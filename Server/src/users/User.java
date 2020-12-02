package users;

import java.util.Objects;

public class User {
    private final String login;
    private final String password;
    private final String username;

    //По хорошему этих юзеров надо хранить в БД и из БД подтягивать
    public User(String login, String password, String username) {
        this.login = login;
        this.password = password;
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || getClass() !=obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password)
                && Objects.equals(username, user.username);
    }
}
