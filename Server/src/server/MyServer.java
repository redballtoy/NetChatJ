package server;

import autoidentification.AuthService;
import autoidentification.BaseAuthService;
import autoidentification.Prefics;
import handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthService authService;

    //коллекция хендлеров по которой будем проверять что никнейм свободен
    //это фактически список подключенных пользователей которым надо передавать сообщения
    private final List<ClientHandler> clients = new ArrayList<>();


    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        //создание класса аутентификации на основе интерфейса
        this.authService = new BaseAuthService();
    }

    //создаем геттер сервера аутентификации что бы иметь к нему доступ
    public AuthService getAuthService() {
        return authService;
    }

    public void start() {
        System.out.println("Сервер запущен!");

        //запускаем аутентификацию
        authService.start();

        //ожидание подключения
        try {
            while (true) {
                newClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка создания нового подключения");
        } finally {
            try {
                //закрываем сокет при ошибке
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка закрытия сокета");
            }

        }
    }

    private void newClientConnection() throws IOException {
        System.out.println("Ожидание пользователя...");
        //ждем подключения пользователя и получения его сокета
        Socket clientSocket = serverSocket.accept();
        System.out.println("Пользователь подключился");

        //создаем для него новый хендлер, который будет создавать поток для данного пользователя на
        //на основе его сокета
        processClientConnection(clientSocket);
    }

    //создание потока подключения пользователя к серверу
    private void processClientConnection(Socket clientSocket) throws IOException {
        //создание нового хендлера
        //this - это сссылка на MyServer который будет связывать хандлер, идентификацию и логику самого сервера
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);

        //метод создания все логики подключения и создания потоков
        clientHandler.handle();
    }

    //Подписаться на наше подключение
    public void subscribe(ClientHandler clientHandler) {
        //будет добавлять клиентов в хандлер
        clients.add(clientHandler);
    }

    //Отписаться от нашего подключения
    public void unsubscribe(ClientHandler clientHandler) {
        //будет добавлять клиентов в хандлер
        clients.remove(clientHandler);
    }

    public boolean isUsernetBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    //отправить сообщение всем что подключмлся новичок
    public void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                //если клиент является отправителем мы его игнорируем
                continue;
            }
            //если это другой пользователь то мы отправим ему сообщение
            client.sendMessage(isServerInfoMsg(message) ? null : sender.getUsername(), message);


        }

    }

    private boolean isServerInfoMsg(String message) {
        String[] msg = message.split(Prefics.STRING_SPLIT_PREFIX.getCode(), 1);
        return msg[0].equals(Prefics.SERVER_MSG_PREFIX.getCode());
    }
}
