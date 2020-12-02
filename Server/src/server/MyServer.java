package server;

import autoidentification.AuthService;
import autoidentification.BaseAuthService;

import java.io.IOException;
import java.net.ServerSocket;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthService authService;

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        //создание класса аутентификации на основе интерфейса
        this.authService = new BaseAuthService();
    }

    public void start() {
        System.out.println("Сервер запущен!");
    }
}
