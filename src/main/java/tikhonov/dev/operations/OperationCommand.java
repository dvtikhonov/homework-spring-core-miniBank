package tikhonov.dev.operations;

/**
 * Обработчик одной консольной команды.
 */
public interface OperationCommand {

    /**
     * Читает параметры и выполняет операцию.
     * При EOF выбрасывает {@link tikhonov.dev.console.EndOfInputException} до изменения счетов.
     */
    void execute();

    /**
     * @return тип команды, который обслуживает реализация
     */
    ConsoleOperationType getOperationType();
}
