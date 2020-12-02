package autoidentification;

public interface AuthService {
    //старт аутотенфикации
    void start();

    //резульатат если аутентификация прошла, возвращаем username
    String getUsernameByLoginAndPassword(String login, String password);

    //закрытие аутентификации
    void close();

}
