package net;

import autoidentification.Prefics;
import controller.ViewController;
import javafx.scene.control.Alert;
import util.AlertInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//класс который полностью отвечает за взаимодействие между сервером и клиентом (view ) через контроллер
public class Network {
    private static final int SERVER_PORT = 8089;
    private static final String SERVER_HOST = "localhost";//если по сети то тут должен был быть ip адрес

    private final int port;
    private final String host;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private String username;


    final AlertInfo alertInfo = new AlertInfo();

    //будет обращаться к конструктору с дефолтными параметрами
    public Network() {
        this(SERVER_HOST, SERVER_PORT);

    }

    public Network(String serverHost, int serverPort) {
        this.host = serverHost;
        this.port = serverPort;

    }

    //создание сетевого соединения
    public boolean connect() {
        //создание сокета клиента, необходимо будет указать хост и порт
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            //создание потоков
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            alertInfo.alertGo("IOException", "Connection Error"
                    , "Соединение не было установлено", Alert.AlertType.ERROR);
            return false;
        }
    }

    //закрытие сокета что бы избежать ошибок при выключении клиента
    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void waitMessage(ViewController viewController) {
        //ожидание ответв от сервера делаем в новом потоке
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    //сообщение необходимо отправить в контроллер что бы отобразить его
                    //для этого надо получить элемент контроллера
                    viewController.addWordToList(msg);
                    System.out.println("Log:" + msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
                alertInfo.alertGo("IOException", "Connection Error"
                        , "Соединение потеряно", Alert.AlertType.ERROR);

            }

        });
        //демоны выполняются в фоне и имеют небольшой приоритет, мало потребляют ресурсов и закрываются при
        //закрытии приложения
        thread.setDaemon(true);
        thread.start();

    }

    public String sendAuthCommnd(String login, String password) {
        try {
            out.writeUTF(String.format("%s %s %s",
                    Prefics.AUTH_CMD_PREFIX.getCode(),
                    login,
                    password));
        } catch (IOException e) {
            e.printStackTrace();
            new AlertInfo().alertGo("Send info to server Error!"
                    , "Ошибка отправки данныз пользователя на сервер",
                    "Попробовать еще раз", Alert.AlertType.ERROR);
        }
        //Должны получить ответ от сервера
        String responce = null;
        try {
            responce = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            new AlertInfo().alertGo("Server responce Error!"
                    , "Ошибка получения данных аутентификации с сервера",
                    "Попробовать еще раз", Alert.AlertType.ERROR);
        }

        //Проверяем префиксы
        if (responce.startsWith(Prefics.AUTH_CMD_PREFIX.getCode())) {
            //Ок продолжаем процедуру ауторизации
            //Парсим ответ от сервера
            this.username = responce.split(Prefics.STRING_SPLIT_PREFIX.getCode(), 2)[1];
            new AlertInfo().alertGo("Auth successfully!", "Аутодентификация пройдена",
                    "Пользователь: " + username + " авторизован!", Alert.AlertType.INFORMATION);
            //ошибки нет
            return null;
        }
        return responce.split(Prefics.STRING_SPLIT_PREFIX.getCode(), 2)[1];
    }
}
