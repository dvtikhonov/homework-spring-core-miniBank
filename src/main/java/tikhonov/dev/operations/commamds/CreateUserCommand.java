package tikhonov.dev.operations.commamds;

import tikhonov.dev.account.AccountService;
import tikhonov.dev.console.ConsoleInput;
import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import tikhonov.dev.user.User;
import tikhonov.dev.user.UserService;
import org.springframework.stereotype.Component;

/**
 * Создает пользователя и сразу открывает ему счет с начальным балансом.
 */
@Component
public class CreateUserCommand implements OperationCommand {

    private final ConsoleInput consoleInput;
    private final UserService userService;
    private final AccountService accountService;

    /**
     * @param consoleInput   чтение логина
     * @param userService    создание пользователя
     * @param accountService открытие первого счета
     */
    public CreateUserCommand(
            ConsoleInput consoleInput,
            UserService userService,
            AccountService accountService
    ) {
        this.consoleInput = consoleInput;
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        String login = consoleInput.readLine("Enter login:").trim();
        User user = userService.createUser(login);
        accountService.createAccount(user.getId());
        System.out.println("User created: " + user);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
