package tikhonov.dev;

import tikhonov.dev.config.ApplicationConfiguration;
import tikhonov.dev.console.OperationsConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Точка входа: поднимает Spring-контекст и запускает консольный цикл.
 */
public final class Main {

    private Main() {
    }

    /**
     * @param args не используются
     */
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            context.getBean(OperationsConsoleListener.class).listen();
        } catch (RuntimeException ex) {
            System.out.println("Error: " + rootMessage(ex));
            System.exit(1);
        }
    }

    private static String rootMessage(Throwable error) {
        Throwable current = error;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        String message = current.getMessage();
        if (message == null || message.isBlank()) {
            return current.toString();
        }
        return message;
    }
}
