package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.Network;
import util.AlertInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//перехватывает действия и реагирует на них
public class ViewController {


    //связывание контроллера с элементами вьюхи
    @FXML
    private TextField et_edit_text;

    @FXML
    private Button bt_send_text;

    @FXML
    private MenuBar m_top_menu;


    @FXML
    private ListView<String> lv_output_word;

    //Для хранения textView нужна коллекция приспособленная дла fx
    private final ObservableList<String> wordList = FXCollections.observableArrayList();

    @FXML
    private ListView<String> lv_users_list;
    //Для хранения textView нужна коллекция приспособленная дла fx
    private final ObservableList<String> usersList = FXCollections.observableArrayList(
            "Андрей", "Сергей", "Василий");

    //статусная строка
    @FXML
    private ListView<String> lv_status_line;
    private final ObservableList<String> statusLine = FXCollections.observableArrayList(
            "Строка статуса...");

    //для взаимодействия с классом Network его необходимо получить
    private Network network;

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void initialize() {
        //вносим данные коллекции в ListView
        //bt_send_text.setText("SetMessages");
        et_edit_text.setText("Привет!");
        lv_output_word.setItems(wordList);
        lv_users_list.setItems(usersList);
        lv_status_line.setItems(statusLine);
    }

    //Добавление информации в строку статуса
    public void addToStatusLine(String msg, Alert.AlertType type) {
        if (type == Alert.AlertType.INFORMATION) {
            lv_status_line.setStyle("-fx-control-inner-background: " + "derive(palegreen, 50%)" + ";");

            //lv_status_line.setStyle("-fx-font-size: 16px; -fx-text-inner-color: red");
        } else {
            //lv_status_line.setStyle("-fx-font-size: 16px; -fx-text-inner-color: green");
            lv_status_line.setStyle("-fx-control-inner-background: " + "derive(red, 50%)" + ";");
        }
        //lv_status_line.setStyle("-fx-control-inner-background: " + "derive(orange, 50%)" + ";");
        //lv_status_line.setStyle("-fx-font-size: 16px; -fx-text-inner-color: orange");
        statusLine.add(msg);

    }

    @FXML
    //Добавление слова в список
    public void addWordToList(String word) {
        //Валидация что в окно ввода не пустое
        if (word.isEmpty()) {
            new AlertInfo().alertGo("Input Error!", "Ошибка ввода сообщения"
                    , "Вы не ввели сообщение!\nНельзя вводить пустое сообщение!", Alert.AlertType.ERROR);
        } else {
            //Получаем коллекцию элементов из ListView и добавляем в нее то что вводим внизу
            lv_output_word.getItems().add(word);

        }
        //Очищаем поле et_edit_text
        et_edit_text.clear();
    }

    @FXML
    //Добавление слова в список
    public void addWord() {
        //Получем слово введенное в input поле
        String word = et_edit_text.getText().toString();
        //Вадидация что в осно ввода не пустое
        if (word.isEmpty()) {
            new AlertInfo().alertGo("Input Error!", "Ошибка ввода сообщения"
                    , "Вы не ввели сообщение!\nНельзя вводить пустое сообщение!", Alert.AlertType.ERROR);
        } else {
            //Получаем коллекцию элементов из ListView и добавляем в нее то что вводим внизу
            if (getCurrentUser() == null) {
                new AlertInfo().alertGo("Select Error","Ошибка выбора пользователя"
                        ,"Ваберите кому хотите написать", Alert.AlertType.ERROR);
                return;
            }

            String str = getCurrentTime() +  " Вы: -> " + getCurrentUser() + " : " + word;
            addWordToList(str);
            sendMessage(word);
        }
        //Очищаем поле et_edit_text
        et_edit_text.clear();
    }

    //Получение выделенного пользователя
    @FXML
    public String getCurrentUser() {
        return lv_users_list.getSelectionModel().getSelectedItem();
    }


    //Выход в меню
    @FXML
    public void exit() {
        System.exit(1);
    }



    @FXML //если есть связь с шаблоном обязательно указывать
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("HW for Lesson 6");
        alert.setContentText("FX приложение созданное в рамках 6 урока!");
        alert.showAndWait();//отображает окно и не дает с него переключаться в отличие от простого show
    }

    private String getCurrentTime() {
        String formatDateTime = "dd.mm.yy hh:mm:ss";
        String formatTime = "hh:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat(formatTime);
        return sf.format(new Date());
    }

    private void sendMessage(String msg){
        try {
            network.getOut().writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Ошибка при отправке сообщения " + msg);
            addToStatusLine("Error#2 - Ошибка отправки сообщения", Alert.AlertType.ERROR);
        }


    }
}
