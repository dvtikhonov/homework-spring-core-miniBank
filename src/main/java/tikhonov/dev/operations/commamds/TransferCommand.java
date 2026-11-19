package tikhonov.dev.operations.commamds;

import tikhonov.dev.account.AccountService;
import tikhonov.dev.account.TransferResult;
import tikhonov.dev.console.ConsoleInput;
import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import org.springframework.stereotype.Component;

/**
 * Переводит деньги между счетами.
 */
@Component
public class TransferCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final AccountService accountService;

    /**
     * @param consoleInput   чтение параметров
     * @param accountService перевод
     */
    public TransferCommand(ConsoleInput consoleInput, AccountService accountService) {
        this.consoleInput = consoleInput;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        int sourceAccountId = consoleInput.readInt("Enter source account id:");
        int targetAccountId = consoleInput.readInt("Enter target account id:");
        int amount = consoleInput.readInt("Enter amount:");
        TransferResult result = accountService.transfer(sourceAccountId, targetAccountId, amount);
        if (result.sameUser()) {
            System.out.println(
                    "Transfer completed from account " + result.sourceAccountId()
                            + " to account " + result.targetAccountId()
                            + ". Amount: " + result.amount()
                            + " (no commission, same user)"
            );
            return;
        }
        System.out.println(
                "Transfer completed from account " + result.sourceAccountId()
                        + " to account " + result.targetAccountId()
                        + ". Amount: " + result.amount()
                        + ", commission: " + result.commission()
                        + ", recipient received: " + result.received()
        );
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
