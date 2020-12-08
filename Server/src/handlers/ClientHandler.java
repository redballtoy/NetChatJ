package handlers;

import autoidentification.AuthService;
import autoidentification.Prefics;
import server.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    //метод handle будет работать с локальными полями поэтому необходимо их создать
    private final MyServer myServer;
    private final Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        //заполнить локальные поля ссфлками на сервер и сокет данного клиента
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public String getUsername() {
        return username;
    }

    public void handle() throws IOException {
        //Создает входные и выходные потоки
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        //создаем новый поток
        new Thread(() -> {
            //аутентификация
            try {
                authefication();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка идентификации!");

            }

            //если аутентификация пройдет то дальше пойдет взаимодействие с клиентом
            // ожидание и чтение сообщений


        }).start();


    }

    private void authefication() throws IOException {
        //во время аутенфикации клиент отправит нам логин и пароль
        //получаем сообщение об аутенфикации
        String message = in.readUTF();

        //авторизируйся!
        while (true) {
            if (message.startsWith(Prefics.AUTH_CMD_PREFIX.getCode())) {
                //начинается процесс аутотентивикации
                //для этого распарсим полученное сообщение
                //регулярное выражение что разделителем является один или несколько пробелов и
                //и сообщение будем делить на 3 части
                String[] parts = message.split(Prefics.STRING_SPLIT_PREFIX.getCode(), 3);
                String login = parts[1];
                String password = parts[2];

                //обращаемся к серверу аутентификации что бы проверить логин и пароль
                AuthService authService = myServer.getAuthService();
                username = authService.getUsernameByLoginAndPassword(login, password);
                if (username!=null) {
                    //значит авторизация прошла
                    //проверка не занят ли никнейм
                    if (myServer.isUsernetBusy(username)) {
                        //System.out.printf("Никнейм %s уже занят", username);
                        out.writeUTF(String.format("%s %s", Prefics.AUTHERR_CMD_PREFIX.getCode(),
                                "Никнейм  уже занят"));

                    }

                    //отправим на клиент никнейм
                    out.writeUTF(String.format("%s %s", Prefics.AUTHOK_CMD_PREFIX.getCode(), username));


                    //оповестить о подключении новичка всех других пользователей
                    myServer.broadcastMessage(String.format("%s %s подключился к чату"
                            , Prefics.SERVER_MSG_PREFIX.getCode(), username), this);

                    //зарегистрировать клиента через подписку на MyServer
                    //т.е. он появляется в списке подключенных к серверу пользователей
                    myServer.subscribe(this); //на вход отдаем текущий хендлер
                } else {
                    out.writeUTF(String.format("%s %s", Prefics.AUTHERR_CMD_PREFIX.getCode(),
                            "Ошибка логина или пароля"));

                }
            }else {
                out.writeUTF(String.format("%s %s", Prefics.AUTHERR_CMD_PREFIX.getCode(),
                        "Ошибка авторизации"));
            }
        }
    }

    public void sendMessage(String sender, String message) throws IOException {
        if (sender == null) {
            out.writeUTF(String.format("%s %s", Prefics.SERVER_MSG_PREFIX.getCode(), message));
        } else {
            //если это не серверное сообщение, значит это сообщение в чат,
            // добавляем sender - кто отправил сообщение
            out.writeUTF(String.format("%s %s %s", Prefics.CLIENT_MSG_PREFIX.getCode(), sender, message));
        }

    }
}
