package tikhonov.dev.console;

import tikhonov.dev.operations.ConsoleOperationType;
import tikhonov.dev.operations.OperationCommand;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Цикл консольного меню: читает команду и вызывает соответствующий обработчик.
 */
@Component
public class OperationsConsoleListener {

    private final ConsoleInput consoleInput;
    private final Map<ConsoleOperationType, OperationCommand> commandMap;

    /**
     * @param commands     все обработчики команд из контекста
     * @param consoleInput чтение команд
     */
    public OperationsConsoleListener(List<OperationCommand> commands, ConsoleInput consoleInput) {
        this.consoleInput = consoleInput;
        Map<ConsoleOperationType, OperationCommand> mapped = new EnumMap<>(ConsoleOperationType.class);
        for (OperationCommand command : commands) {
            OperationCommand previous = mapped.put(command.getOperationType(), command);
            if (previous != null) {
                throw new IllegalStateException("duplicate command for " + command.getOperationType());
            }
        }
        this.commandMap = Map.copyOf(mapped);
    }

    /**
     * Проверяет, что для каждой операции, кроме выхода, есть обработчик.
     */
    @PostConstruct
    void ensureAllCommandsRegistered() {
        for (ConsoleOperationType type : ConsoleOperationType.values()) {
            if (type != ConsoleOperationType.EXIT && !commandMap.containsKey(type)) {
                throw new IllegalStateException("missing command handler: " + type);
            }
        }
    }

    /**
     * Работает, пока пользователь не введет EXIT или ввод не закончится.
     */
    public void listen() {
        System.out.println("MiniBank started. Type EXIT to stop.");
        System.out.println("Available commands: " + Stream.of(ConsoleOperationType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", ")));
        while (true) {
            String line;
            try {
                line = consoleInput.readLine("Enter command:");
            } catch (EndOfInputException ex) {
                break;
            }
            String command = line.trim();
            if (command.isEmpty()) {
                System.out.println("Error: command must not be empty");
                continue;
            }
            ConsoleOperationType type;
            try {
                type = ConsoleOperationType.valueOf(command.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: unknown command: " + command);
                continue;
            }
            if (type == ConsoleOperationType.EXIT) {
                break;
            }
            try {
                commandMap.get(type).execute();
            } catch (EndOfInputException ex) {
                break;
            } catch (RuntimeException ex) {
                String message = ex.getMessage();
                System.out.println("Error: " + (message == null || message.isBlank()
                        ? ex.getClass().getSimpleName()
                        : message));
            }
        }
        System.out.println("MiniBank stopped.");
    }
}
