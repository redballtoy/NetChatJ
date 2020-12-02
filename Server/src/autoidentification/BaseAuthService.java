package autoidentification;

import users.User;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    //список пользователей загерестрированных на сайте
    private static final List<User> clients = List.of(
            new User("user1", "1111", "Федор_Михайлович"),
            new User("user2", "2222", "Борис_Николаевич"),
            new User("user3", "3333", "Гендальф_Серый")
    );

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен!");

    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        //пробежимся и сопоставим логин и пароль
        for (User client : clients) {
            if (client.getLogin().equals(login) && client.getPassword().equals(password)) {
                return client.getUsername();
            }
        }
        return null;
    }

    @Override
    public void close() {
        System.out.println("Сервис аутентификации остановлен!");

    }
}
