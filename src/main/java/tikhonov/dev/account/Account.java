package tikhonov.dev.account;

/**
 * Банковский счет. Обычный POJO: Spring-бином не является.
 */
public class Account {

    private int id;
    private int userId;
    private int moneyAmount;

    /**
     * @param id          уникальный идентификатор счета
     * @param userId      идентификатор владельца
     * @param moneyAmount текущий баланс
     */
    public Account(int id, int userId, int moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", userId=" + userId + ", moneyAmount=" + moneyAmount + "}";
    }
}
