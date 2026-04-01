package org.nahap.strore.presentation.console;

import org.nahap.strore.application.exception.NotFoundException;
import org.nahap.strore.application.exception.ValidationException;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.presentation.console.command.ConsoleCommand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final ConsoleInput input;
    private final DepartmentConsoleActions departmentActions;
    private final ProductConsoleActions productActions;
    private final Map<Integer, ConsoleCommand> commands;

    public ConsoleMenu(DepartmentService departmentService, ProductService productService) {
        this.input = new ConsoleInput(new Scanner(System.in));
        this.departmentActions = new DepartmentConsoleActions(departmentService, input);
        this.productActions = new ProductConsoleActions(productService, input);
        this.commands = createCommands();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int command = input.readInt("Выберите действие: ");
            try {
                ConsoleCommand menuCommand = commands.get(command);
                if (menuCommand == null) {
                    System.out.println("Неизвестная команда");
                } else {
                    running = menuCommand.execute();
                }
            } catch (ValidationException | NotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println();
        }
    }

    private Map<Integer, ConsoleCommand> createCommands() {
        Map<Integer, ConsoleCommand> map = new LinkedHashMap<>();
        map.put(1, () -> {
            departmentActions.createDepartment();
            return true;
        });
        map.put(2, () -> {
            departmentActions.showAllDepartments();
            departmentActions.updateDepartment();
            return true;
        });
        map.put(3, () -> {
            departmentActions.showAllDepartments();
            departmentActions.deleteDepartment();
            return true;
        });
        map.put(4, () -> {
            departmentActions.showAllDepartments();
            productActions.createProduct();
            return true;
        });
        map.put(5, () -> {
            productActions.showAllProducts();
            departmentActions.showAllDepartments();
            productActions.updateProduct();
            return true;
        });
        map.put(6, () -> {
            productActions.showAllProducts();
            productActions.deleteProduct();
            return true;
        });
        map.put(7, () -> {
            departmentActions.showAllDepartments();
            productActions.showProductsByDepartment();
            return true;
        });
        map.put(8, () -> {
            departmentActions.showDepartmentsWithoutProducts();
            return true;
        });
        map.put(9, () -> {
            productActions.showAllProducts();
            return true;
        });
        map.put(10, () -> {
            departmentActions.showAllDepartments();
            return true;
        });
        map.put(0, () -> {
            System.out.println("Выход");
            return false;
        });
        return map;
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
}