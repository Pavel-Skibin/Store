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
    private enum MenuOption {
        CREATE_DEPARTMENT(1, "Добавить отдел"),
        UPDATE_DEPARTMENT(2, "Редактировать отдел"),
        DELETE_DEPARTMENT(3, "Удалить отдел"),
        CREATE_PRODUCT(4, "Добавить товар"),
        UPDATE_PRODUCT(5, "Редактировать товар"),
        DELETE_PRODUCT(6, "Удалить товар"),
        SHOW_PRODUCTS_BY_DEPARTMENT(7, "Показать товары в отделе"),
        SHOW_DEPARTMENTS_WITHOUT_PRODUCTS(8, "Показать отделы без товаров"),
        SHOW_ALL_PRODUCTS(9, "Показать все товары"),
        SHOW_ALL_DEPARTMENTS(10, "Показать все отделы"),
        EXIT(0, "Выход");

        private final int code;
        private final String label;

        MenuOption(int code, String label) {
            this.code = code;
            this.label = label;
        }

        int code() {
            return code;
        }

        String label() {
            return label;
        }
    }

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
        map.put(MenuOption.CREATE_DEPARTMENT.code(), () -> {
            departmentActions.createDepartment();
            return true;
        });
        map.put(MenuOption.UPDATE_DEPARTMENT.code(), () -> {
            departmentActions.showAllDepartments();
            departmentActions.updateDepartment();
            return true;
        });
        map.put(MenuOption.DELETE_DEPARTMENT.code(), () -> {
            departmentActions.showAllDepartments();
            departmentActions.deleteDepartment();
            return true;
        });
        map.put(MenuOption.CREATE_PRODUCT.code(), () -> {
            departmentActions.showAllDepartments();
            productActions.createProduct();
            return true;
        });
        map.put(MenuOption.UPDATE_PRODUCT.code(), () -> {
            productActions.showAllProducts();
            departmentActions.showAllDepartments();
            productActions.updateProduct();
            return true;
        });
        map.put(MenuOption.DELETE_PRODUCT.code(), () -> {
            productActions.showAllProducts();
            productActions.deleteProduct();
            return true;
        });
        map.put(MenuOption.SHOW_PRODUCTS_BY_DEPARTMENT.code(), () -> {
            departmentActions.showAllDepartments();
            productActions.showProductsByDepartment();
            return true;
        });
        map.put(MenuOption.SHOW_DEPARTMENTS_WITHOUT_PRODUCTS.code(), () -> {
            departmentActions.showDepartmentsWithoutProducts();
            return true;
        });
        map.put(MenuOption.SHOW_ALL_PRODUCTS.code(), () -> {
            productActions.showAllProducts();
            return true;
        });
        map.put(MenuOption.SHOW_ALL_DEPARTMENTS.code(), () -> {
            departmentActions.showAllDepartments();
            return true;
        });
        map.put(MenuOption.EXIT.code(), () -> {
            System.out.println("Выход");
            return false;
        });
        return map;
    }

    private void printMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.code() + ". " + option.label());
        }
    }
}