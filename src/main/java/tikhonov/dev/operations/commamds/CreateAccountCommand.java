package tikhonov.dev.operations.commamds;

import tikhonov.dev.account.Account;
import tikhonov.dev.account.AccountService;
import tikhonov.dev.console.ConsoleInput;
import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import org.springframework.stereotype.Component;

/**
 * Открывает дополнительный счет существующему пользователю.
 */
@Component
public class CreateAccountCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final AccountService accountService;

    /**
     * @param consoleInput   чтение id пользователя
     * @param accountService создание счета
     */
    public CreateAccountCommand(ConsoleInput consoleInput, AccountService accountService) {
        this.consoleInput = consoleInput;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        int userId = consoleInput.readInt("Enter user id:");
        Account account = accountService.createAccount(userId);
        System.out.println("Account created: " + account);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
