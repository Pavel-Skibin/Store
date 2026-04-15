package org.nahap.strore.presentation.console;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;
    private final DateTimeFormatter timeFormatter;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    public int readInt(String prompt) {
        while (scanner.hasNextLine()) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите целое число");
            }
        }
        throw new IllegalStateException("Input is not available");
    }

    public BigDecimal readPrice(String prompt) {
        while (scanner.hasNextLine()) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return new BigDecimal(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректную цену");
            }
        }
        throw new IllegalStateException("Input is not available");
    }

    public String readText(String prompt) {
        while (scanner.hasNextLine()) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            }
            System.out.println("Поле не может быть пустым");
        }
        throw new IllegalStateException("Input is not available");
    }

    public LocalTime readLocalTime(String prompt) {
        while (scanner.hasNextLine()) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalTime.parse(input.trim(), timeFormatter);
            } catch (DateTimeParseException ex) {
                System.out.println("Введите время в формате HH:mm (например, 09:30)");
            }
        }
        throw new IllegalStateException("Input is not available");
    }

    public DateTimeFormatter timeFormatter() {
        return timeFormatter;
    }
}