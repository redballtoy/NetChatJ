package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Client;
import net.Network;

public class AuthViewController {

    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;


    //добавляем Network что бы через него идентифицироваться
    private Network network;
    private Client networkClient;

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(Client networkClient) {
        this.networkClient = networkClient;
    }
}
