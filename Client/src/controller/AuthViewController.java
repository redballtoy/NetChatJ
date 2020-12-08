package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Client;
import net.Network;
import util.AlertError;

public class AuthViewController {

    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;


    //добавляем Network что бы через него идентифицироваться
    private Network network;
    private Client networkClient;

    @FXML
    //эту функцию для кнопки которая будет вызываться при нажатии задали в
    //SceneBuilder
    public void checkAuth() {
        String login = loginField.getText();
        String password = passwordField.getText();

        //Если поля не пустые, передаем данные не Network а в свою очередь на сервер
        //там они будут проверены на валидность
        if (login.isEmpty() || password.isEmpty()) {
            new AlertError().alertGo("EmptyField","Не введены пароль или логин",
                    "Введите логин и пароль");
            return;
        }

        //отправить параметры на авторизацию и вернуть результат
        //String authErrorMessage = network.sendAuthCommnd(login, password);
        //


    }


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(Client networkClient) {
        this.networkClient = networkClient;
    }
}
