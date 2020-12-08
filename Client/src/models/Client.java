package models;//вставить в переменную
//--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

import controller.AuthViewController;
import controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.Network;

import java.io.IOException;

public class Client extends Application {

    final String PATH_TO_MAIN_XML_LAYOUT = "/views/view.fxml";
    final String PATH_TO_AUTH_XML_LAYOUT = "/views/auth_view.fxml";
    final int width = 1000;
    final int height = 600;
    final int VERSION_APP = 6;
    private Stage primaryStage;
    private Stage authStage;
    private Network network;
    private ViewController viewController;


    //Единственная цель этого метода запуск приложения
    public static void main(String[] args) {
        launch(args);//будет запускать метод start
    }

    @Override
    //Stage - это сцена, фактически это окно JavaFX
    public void start(Stage primaryStage) throws IOException {

        //Stage для основного чата
        this.primaryStage = primaryStage;

        //Создаем Network
        //Получение network и взаимодействие с ним
        network = new Network();
        //если соединение не произошло вывести сообщение
        if (!network.connect()) {

            alertGo("Connect Input Error", "Ошибка подключения к серверу",
                    "Ошибка подключения к серверу", Alert.AlertType.ERROR);
            return;
        }

        //Открытие окна с формой авторизации
        openAuthWindow();

        //открытие чата при успешном прохождении авторизации
        createMainChatWindow();

    }

    //метод открывающий на экране окно авторизации
    public void openAuthWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(PATH_TO_AUTH_XML_LAYOUT));
        Parent root = loader.load();

        //создание модального окна
        authStage = new Stage();
        authStage.setTitle("Авторизация");
        authStage.initModality(Modality.WINDOW_MODAL);
        //определяем к чему будем прикреплять модальное окно
        authStage.initOwner(primaryStage);
        //сцена для отображения нового окна
        Scene authScene = new Scene(root);
        //привязываем Stage к сцене
        authStage.setScene(authScene);
        //отображаем на экране
        authStage.show();

        //контроллер получает объект из лоадера
        AuthViewController authViewController = loader.getController();
        authViewController.setNetwork(network);
        //добавляем текущий объект
        authViewController.setNetworkClient(this);

       // authStage.setOnCloseRequest(windowEvent -> network.closeSocket());


    }

    //Окно открытия основного чата
    public void createMainChatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(PATH_TO_MAIN_XML_LAYOUT));

        //создание окна для отображения
        // Parent - это обобщенный класс отображаемый для всех компонентов JavaFX
        //это корневой шаблон который мы видим в окне сцены,
        Parent root = loader.load();
        primaryStage.setTitle("FXmessenger v." + VERSION_APP);
        primaryStage.setScene(new Scene(root, width, height));
        //primaryStage.show(); //не должно появляться по умолчаниж

        //Получение контроллера для вьюхи
        viewController = loader.getController();
        //в контроллер передаем объект нашего network
        viewController.setNetwork(network);
        //Чтение сообщения с сервера
        //network.waitMessage(viewController);
        //В случае закрытия нашего окна закрываем сокет
        //используем лямбду для реализации функционального интерфейса
        primaryStage.setOnCloseRequest(windowEvent -> network.closeSocket());
    }

    public void openMainChatWindow() {
        //звкрыть окно аутентификации
        authStage.close();
        //открыть окно основного чата
        primaryStage.show();

        //Выводим имя подключившегося юзера
        primaryStage.setTitle(network.getUsername());
        viewController.setUsernameTitle(network.getUsername());
        //ожидание нашего чата
        network.waitMessage(viewController);


    }

    public static void alertGo(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }
}
