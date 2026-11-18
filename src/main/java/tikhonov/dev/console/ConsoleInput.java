package tikhonov.dev.console;

import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Единая точка чтения консоли.
 */
@Component
public class ConsoleInput {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Печатает приглашение и читает следующую строку.
     *
     * @param prompt текст приглашения
     * @return строка без перевода строки
     * @throws EndOfInputException если ввод закончился
     */
    public String readLine(String prompt) {
        System.out.println(prompt);
        if (!scanner.hasNextLine()) {
            throw new EndOfInputException();
        }
        return scanner.nextLine();
    }

    /**
     * @param prompt приглашение
     * @return целое число из строки
     * @throws EndOfInputException      если ввод закончился
     * @throws IllegalArgumentException если строка не целое число
     */
    public int readInt(String prompt) {
        String line = readLine(prompt).trim();
        if (line.isEmpty()) {
            throw new IllegalArgumentException("expected an integer, got empty input");
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("expected an integer, got: '" + line + "'");
        }
    }
}
