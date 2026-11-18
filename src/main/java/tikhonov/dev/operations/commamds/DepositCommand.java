package tikhonov.dev.operations.commamds;

import tikhonov.dev.account.AccountService;
import tikhonov.dev.console.ConsoleInput;
import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import org.springframework.stereotype.Component;

/**
 * Пополняет счет.
 */
@Component
public class DepositCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final AccountService accountService;

    /**
     * @param consoleInput   чтение параметров
     * @param accountService пополнение
     */
    public DepositCommand(ConsoleInput consoleInput, AccountService accountService) {
        this.consoleInput = consoleInput;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readInt("Enter account id:");
        int amount = consoleInput.readInt("Enter amount:");
        int balance = accountService.deposit(accountId, amount);
        System.out.println("Deposited " + amount + " to account " + accountId + ". New balance: " + balance);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
