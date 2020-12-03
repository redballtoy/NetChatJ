package net;

import controller.ViewController;
import util.AlertError;

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

    final AlertError alertError = new AlertError();

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
            alertError.alertGo("IOException", "Connection Error"
                    , "Соединение не было установлено");
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
                alertError.alertGo("IOException", "Connection Error"
                        , "Соединение потеряно");
            }

        });
        //демоны выполняются в фоне и имеют небольшой приоритет, мало потребляют ресурсов и закрываются при
        //закрытии приложения
        thread.setDaemon(true);
        thread.start();

    }
}
