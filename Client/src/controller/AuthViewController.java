package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Client;
import net.Network;
import util.AlertInfo;

import java.io.IOException;

public class AuthViewController {

    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;


    //добавляем Network что бы через него идентифицироваться
    private Network network;
    private Client networkClient;

    @FXML
    //метод проверки аутентификации
    //эту функцию для кнопки которая будет вызываться при нажатии задали в
    //SceneBuilder
    public void checkAuth() throws IOException {
        String login = loginField.getText();
        String password = passwordField.getText();

        //Если поля не пустые, передаем данные не Network а в свою очередь на сервер
        //там они будут проверены на валидность
        if (login.isEmpty() || password.isEmpty()) {
            new AlertInfo().alertGo("EmptyField","Не введены пароль или логин",
                    "Введите логин и пароль", Alert.AlertType.INFORMATION);
            return;
        }

        //отправить параметры на авторизацию и вернуть результат
        String authErrorMessage = network.sendAuthCommnd(login, password);
        System.out.println(authErrorMessage);
        //Если будет ошибка
        if (authErrorMessage!=null) {
            //вызываем окно
            new AlertInfo().alertGo("Error Authentication",
                    "Ошибка идентификации",
                    "Повторите ввод логина и пароля", Alert.AlertType.ERROR);
        } else {
            //Если aвторизация пройдена, открывает чат
            networkClient.openMainChatWindow();
        }


    }


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(Client networkClient) {
        this.networkClient = networkClient;
    }
}
