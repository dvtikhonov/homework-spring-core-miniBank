package tikhonov.dev.user;

import tikhonov.dev.account.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь банка. Обычный POJO: Spring-бином не является.
 */
public class User {

    private int id;
    private String login;
    private List<Account> accountList;

    /**
     * @param id          уникальный идентификатор
     * @param login       логин
     * @param accountList счета пользователя; сохраняется изменяемая копия
     */
    public User(int id, String login, List<Account> accountList) {
        this.id = id;
        this.login = login;
        this.accountList = accountList == null ? new ArrayList<>() : new ArrayList<>(accountList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList == null ? new ArrayList<>() : new ArrayList<>(accountList);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", login='" + login + "', accounts=" + accountList + "}";
    }
}
