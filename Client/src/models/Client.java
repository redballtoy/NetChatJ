package models;//вставить в переменную
//--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

import controller.ViewController;
import net.Network;
import util.AlertError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    Parent root; //это корневой шаблон который мы видим в окне сцены,
    // Parent - это обобщенный класс отображаемый для всех компонентов JavaFX

    final String PATH_TO_XML_LAYOUT = "/views/view.fxml";
    final int width = 1000;
    final int height = 600;
    final int VERSION_APP = 6;
    final AlertError alertError = new AlertError();


    //Единственная цель этого метода запуск приложения
    public static void main(String[] args) {
        launch(args);//будет запускать метод start
    }

    @Override
    //Stage - это сцена, фактически это окно JavaFX
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(PATH_TO_XML_LAYOUT));

        //создание окна для отображения
        root = loader.load();
        primaryStage.setTitle("FXmessenger v." + VERSION_APP);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();



        //Получение network и взаимодействие с ним
        Network network = new Network();
        //если соединение не произошло вывести сообщение
        if (!network.connect()) {
            alertError.alertGo("Connect Input Error","Ошибка подключения к серверу",
                    "Ошибка подключения к серверу");
        }

        //Получение контроллера для вьюхи
        ViewController viewController = loader.getController();
        //в контроллер передаем объект нашего network
        viewController.setNetwork(network);

        //Чтение сообщения с сервера
        network.waitMessage(viewController);

        //В случае закрытия нашего окна закрываем сокет
        //используем лямбду для реализации функционального интерфейса
        primaryStage.setOnCloseRequest(windowEvent->network.closeSocket());





    }

}
