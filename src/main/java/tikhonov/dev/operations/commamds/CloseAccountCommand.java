package tikhonov.dev.operations.commamds;

import tikhonov.dev.account.AccountService;
import tikhonov.dev.account.CloseAccountResult;
import tikhonov.dev.console.ConsoleInput;
import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import org.springframework.stereotype.Component;

/**
 * Закрывает счет и переносит остаток на самый ранний из оставшихся счетов пользователя.
 */
@Component
public class CloseAccountCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final AccountService accountService;

    /**
     * @param consoleInput   чтение id счета
     * @param accountService закрытие
     */
    public CloseAccountCommand(ConsoleInput consoleInput, AccountService accountService) {
        this.consoleInput = consoleInput;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readInt("Enter account id to close:");
        CloseAccountResult result = accountService.close(accountId);
        System.out.println(
                "Account " + result.closedAccountId()
                        + " closed. Remaining balance " + result.transferredAmount()
                        + " transferred to account " + result.targetAccountId() + "."
        );
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
