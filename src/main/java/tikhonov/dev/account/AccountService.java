package tikhonov.dev.account;

import org.springframework.stereotype.Service;
import tikhonov.dev.user.User;
import tikhonov.dev.user.UserService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Операции со счетами. Данные хранятся в памяти.
 */
@Service
public class AccountService {

    private final Map<Integer, Account> accountsById = new LinkedHashMap<>();
    private final UserService userService;
    private final AccountProperties accountProperties;
    private int nextAccountId;

    /**
     * @param userService       поиск владельца счета
     * @param accountProperties начальный баланс и комиссия
     */
    public AccountService(UserService userService, AccountProperties accountProperties) {
        this.userService = userService;
        this.accountProperties = accountProperties;
    }

    /**
     * Открывает счет с балансом из настроек и добавляет его пользователю.
     *
     * @param userId идентификатор владельца
     * @return новый счет
     */
    public Account createAccount(int userId) {
        User user = requireUser(userId);
        if (nextAccountId == Integer.MAX_VALUE) {
            throw new IllegalStateException("account id space is exhausted");
        }
        int id = ++nextAccountId;
        Account account = new Account(id, userId, accountProperties.getDefaultAmount());
        accountsById.put(id, account);
        user.getAccountList().add(account);
        return account;
    }

    /**
     * @param accountId счет
     * @param amount    положительная сумма пополнения
     * @return баланс после пополнения
     */
    public int deposit(int accountId, int amount) {
        requirePositiveAmount(amount);
        Account account = requireAccount(accountId);
        int updated = add(account.getMoneyAmount(), amount, accountId);
        account.setMoneyAmount(updated);
        return updated;
    }

    /**
     * @param accountId счет
     * @param amount    положительная сумма снятия
     * @return баланс после снятия
     */
    public int withdraw(int accountId, int amount) {
        requirePositiveAmount(amount);
        Account account = requireAccount(accountId);
        if (amount > account.getMoneyAmount()) {
            throw new IllegalArgumentException(
                    "insufficient funds on account id=%d, moneyAmount=%d, attempted withdraw=%d"
                            .formatted(account.getId(), account.getMoneyAmount(), amount));
        }
        int updated = subtract(account.getMoneyAmount(), amount, accountId);
        account.setMoneyAmount(updated);
        return updated;
    }

    /**
     * Переводит сумму. Между разными пользователями получатель получает сумму за вычетом комиссии.
     * Оба баланса вычисляются до изменения счетов.
     *
     * @param fromAccountId счет отправителя
     * @param toAccountId   счет получателя
     * @param amount        списываемая сумма
     * @return результат перевода
     */
    public TransferResult transfer(int fromAccountId, int toAccountId, int amount) {
        requirePositiveAmount(amount);
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }
        Account source = requireAccount(fromAccountId);
        Account target = requireAccount(toAccountId);
        if (amount > source.getMoneyAmount()) {
            throw new IllegalArgumentException(
                    "Not enough money to transfer from account: id=%d, moneyAmount=%d, attemptedTransfer=%d"
                            .formatted(source.getId(), source.getMoneyAmount(), amount));
        }
        boolean sameUser = source.getUserId() == target.getUserId();
        int received = sameUser ? amount : amountAfterCommission(amount);
        int sourceAfter = subtract(source.getMoneyAmount(), amount, source.getId());
        int targetAfter = add(target.getMoneyAmount(), received, target.getId());
        source.setMoneyAmount(sourceAfter);
        target.setMoneyAmount(targetAfter);
        return new TransferResult(
                source.getId(),
                target.getId(),
                amount,
                amount - received,
                received,
                sameUser
        );
    }

    /**
     * Закрывает счет и переносит остаток на счет этого пользователя с наименьшим id.
     * Единственный счет пользователя закрыть нельзя.
     *
     * @param accountId закрываемый счет
     * @return куда и сколько перенесено
     */
    public CloseAccountResult close(int accountId) {
        Account account = requireAccount(accountId);
        User user = requireUser(account.getUserId());
        if (user.getAccountList().size() <= 1) {
            throw new IllegalArgumentException(
                    "cannot close the only account of user id=%d".formatted(user.getId()));
        }
        Account target = null;
        for (Account candidate : user.getAccountList()) {
            if (candidate.getId() == accountId) {
                continue;
            }
            if (target == null || candidate.getId() < target.getId()) {
                target = candidate;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException(
                    "cannot close the only account of user id=%d".formatted(user.getId()));
        }
        int remainder = account.getMoneyAmount();
        int targetAfter = add(target.getMoneyAmount(), remainder, target.getId());
        target.setMoneyAmount(targetAfter);
        user.getAccountList().removeIf(item -> item.getId() == accountId);
        accountsById.remove(accountId);
        return new CloseAccountResult(accountId, target.getId(), remainder);
    }

    /**
     * @param id идентификатор счета
     * @return счет, если он открыт
     */
    public Optional<Account> findById(int id) {
        return Optional.ofNullable(accountsById.get(id));
    }

    private User requireUser(int userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No such user: id=%d".formatted(userId)));
    }

    private Account requireAccount(int accountId) {
        return findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%d".formatted(accountId)));
    }

    private static void requirePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
    }

    private int amountAfterCommission(int amount) {
        double received = amount * (1.0d - accountProperties.getTransferCommission());
        long rounded = Math.round(received);
        if (rounded < 0 || rounded > amount) {
            throw new IllegalArgumentException("calculated transfer amount is out of range");
        }
        return (int) rounded;
    }

    private static int add(int balance, int amount, int accountId) {
        try {
            return Math.addExact(balance, amount);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("balance overflow on account id=%d".formatted(accountId));
        }
    }

    private static int subtract(int balance, int amount, int accountId) {
        try {
            return Math.subtractExact(balance, amount);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("balance overflow on account id=%d".formatted(accountId));
        }
    }
}
