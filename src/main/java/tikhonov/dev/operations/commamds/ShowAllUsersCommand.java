package tikhonov.dev.operations.commamds;

import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import tikhonov.dev.user.User;
import tikhonov.dev.user.UserService;
import org.springframework.stereotype.Component;

/**
 * Печатает всех пользователей и их счета.
 */
@Component
public class ShowAllUsersCommand implements OperationCommand {

    private final UserService userService;

    /**
     * @param userService источник списка пользователей
     */
    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        for (User user : userService.findAll()) {
            System.out.println(user);
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
