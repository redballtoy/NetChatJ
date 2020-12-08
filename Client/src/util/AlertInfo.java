package util;

import javafx.scene.control.Alert;

public class AlertInfo {
    public void alertGo(String title, String header, String content,Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        /*alert.setTitle("Input Error!");
        alert.setHeaderText("Ошибка ввода сообщения");
        alert.setContentText("Вы не ввели сообщение!\nНельзя вводить пустое сообщение!");*/
        alert.showAndWait();//отображает окно и не дает с него переключаться в отличие от простого show
    }

}
