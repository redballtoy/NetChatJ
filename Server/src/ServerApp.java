import server.MyServer;

import java.io.IOException;

//Запускающий класс
public class ServerApp {
    public static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {

        //Подключение (поднятие сервера)
        //принимаем порт по умолчанию
        int port = DEFAULT_PORT;

        //принимаем порт с консоли или командной строки
        if (args.length != 0) {
            //получить значение порта
            port = Integer.parseInt(args[0]);
        }

        //Создание MyServer и запусук у него всех методов необходимых для старта
        try {
            new MyServer(port).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка запуска MyServer");
            System.exit(-1);
        }


    }

}
