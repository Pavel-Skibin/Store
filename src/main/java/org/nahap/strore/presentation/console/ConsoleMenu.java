package org.nahap.strore.presentation.console;

import org.nahap.strore.application.exception.NotFoundException;
import org.nahap.strore.application.exception.ValidationException;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.domain.model.Department;
import org.nahap.strore.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final DepartmentService departmentService;
    private final ProductService productService;
    private final Scanner scanner;

    public ConsoleMenu(DepartmentService departmentService, ProductService productService) {
        this.departmentService = departmentService;
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int command = readInt("Выберите действие: ");
            try {
                running = handleCommand(command);
            } catch (ValidationException | NotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println();
        }
    }

    private boolean handleCommand(int command) {
        switch (command) {
            case 1:
                createDepartment();
                return true;
            case 2:
                updateDepartment();
                return true;
            case 3:
                deleteDepartment();
                return true;
            case 4:
                createProduct();
                return true;
            case 5:
                updateProduct();
                return true;
            case 6:
                deleteProduct();
                return true;
            case 7:
                showProductsByDepartment();
                return true;
            case 8:
                showDepartmentsWithoutProducts();
                return true;
            case 9:
                showAllProducts();
                return true;
            case 10:
                showAllDepartments();
                return true;
            case 0:
                System.out.println("Выход");
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }

    private void createDepartment() {
        String name = readText("Название отдела: ");
        LocalTime openTime = readLocalTime("Время открытия (HH:mm): ");
        LocalTime closeTime = readLocalTime("Время закрытия (HH:mm): ");
        Department department = departmentService.createDepartment(name, openTime, closeTime);
        System.out.println("Отдел добавлен: " + formatDepartment(department));
    }

    private void updateDepartment() {
        showAllDepartments();
        int id = readInt("Id отдела: ");
        String name = readText("Новое название: ");
        LocalTime openTime = readLocalTime("Новое время открытия (HH:mm): ");
        LocalTime closeTime = readLocalTime("Новое время закрытия (HH:mm): ");
        Department department = departmentService.updateDepartment(id, name, openTime, closeTime);
        System.out.println("Отдел обновлен: " + formatDepartment(department));
    }

    private void deleteDepartment() {
        showAllDepartments();
        int id = readInt("Id отдела для удаления: ");
        departmentService.deleteDepartment(id);
        System.out.println("Отдел удален");
    }

    private void createProduct() {
        showAllDepartments();
        int departmentId = readInt("Id отдела: ");
        String name = readText("Название товара: ");
        BigDecimal price = readPrice("Цена товара: ");
        Product product = productService.createProduct(departmentId, name, price);
        System.out.println("Товар добавлен: " + formatProduct(product));
    }

    private void updateProduct() {
        showAllProducts();
        int id = readInt("Id товара: ");
        showAllDepartments();
        int departmentId = readInt("Новый id отдела: ");
        String name = readText("Новое название: ");
        BigDecimal price = readPrice("Новая цена: ");
        Product product = productService.updateProduct(id, departmentId, name, price);
        System.out.println("Товар обновлен: " + formatProduct(product));
    }

    private void deleteProduct() {
        showAllProducts();
        int id = readInt("Id товара для удаления: ");
        productService.deleteProduct(id);
        System.out.println("Товар удален");
    }

    private void showProductsByDepartment() {
        showAllDepartments();
        int departmentId = readInt("Id отдела: ");
        List<Product> products = productService.getProductsByDepartment(departmentId);
        if (products.isEmpty()) {
            System.out.println("В отделе нет товаров");
            return;
        }
        for (Product product : products) {
            System.out.println(formatProduct(product));
        }
    }

    private void showDepartmentsWithoutProducts() {
        List<Department> departments = departmentService.getDepartmentsWithoutProducts();
        if (departments.isEmpty()) {
            System.out.println("Отделов без товаров нет");
            return;
        }
        for (Department department : departments) {
            System.out.println(formatDepartment(department));
        }
    }

    private void showAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Товаров нет");
            return;
        }
        for (Product product : products) {
            System.out.println(formatProduct(product));
        }
    }

    private void showAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("Отделов нет");
            return;
        }
        for (Department department : departments) {
            System.out.println(formatDepartment(department));
        }
    }

    private void printMenu() {
        System.out.println("1. Добавить отдел");
        System.out.println("2. Редактировать отдел");
        System.out.println("3. Удалить отдел");
        System.out.println("4. Добавить товар");
        System.out.println("5. Редактировать товар");
        System.out.println("6. Удалить товар");
        System.out.println("7. Показать товары в отделе");
        System.out.println("8. Показать отделы без товаров");
        System.out.println("9. Показать все товары");
        System.out.println("10. Показать все отделы");
        System.out.println("0. Выход");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите целое число");
            }
        }
    }

    private BigDecimal readPrice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return new BigDecimal(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректную цену");
            }
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            }
            System.out.println("Поле не может быть пустым");
        }
    }

    private LocalTime readLocalTime(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalTime.parse(input.trim(), formatter);
            } catch (DateTimeParseException ex) {
                System.out.println("Введите время в формате HH:mm (например, 09:30)");
            }
        }
    }

    private String formatDepartment(Department department) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return department.getId() + " | " + department.getName() + " | " + 
               department.getOpenTime().format(formatter) + " - " + department.getCloseTime().format(formatter);
    }

    private String formatProduct(Product product) {
        return product.getId() + " | " + product.getName() + " | " + product.getPrice().toPlainString() + " | отдел " + product.getDepartmentId();
    }
}