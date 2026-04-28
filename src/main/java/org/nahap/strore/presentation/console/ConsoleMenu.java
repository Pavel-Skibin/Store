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
        CREATE_DEPARTMENT("create-department", "Добавить отдел"),
        UPDATE_DEPARTMENT("update-department", "Редактировать отдел"),
        DELETE_DEPARTMENT("delete-department", "Удалить отдел"),
        CREATE_PRODUCT("create-product", "Добавить товар"),
        UPDATE_PRODUCT("update-product", "Редактировать товар"),
        DELETE_PRODUCT("delete-product", "Удалить товар"),
        SHOW_PRODUCTS_BY_DEPARTMENT("show-products-by-department", "Показать товары в отделе"),
        SHOW_DEPARTMENTS_WITHOUT_PRODUCTS("show-departments-without-products", "Показать отделы без товаров"),
        SHOW_ALL_PRODUCTS("show-all-products", "Показать все товары"),
        SHOW_ALL_DEPARTMENTS("show-all-departments", "Показать все отделы"),
        EXIT("exit", "Выход");

        private final String key;
        private final String label;

        MenuOption(String key, String label) {
            this.key = key;
            this.label = label;
        }

        String key() {
            return key;
        }

        String label() {
            return label;
        }
    }

    private final ConsoleInput input;
    private final DepartmentConsoleActions departmentActions;
    private final ProductConsoleActions productActions;
    private final Map<String, ConsoleCommand> commands;

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
            String command = input.readText("Выберите действие: ").trim().toLowerCase();
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

    private Map<String, ConsoleCommand> createCommands() {
        Map<String, ConsoleCommand> map = new LinkedHashMap<>();
        map.put(MenuOption.CREATE_DEPARTMENT.key(), () -> {
            departmentActions.createDepartment();
            return true;
        });
        map.put(MenuOption.UPDATE_DEPARTMENT.key(), () -> {
            departmentActions.showAllDepartments();
            departmentActions.updateDepartment();
            return true;
        });
        map.put(MenuOption.DELETE_DEPARTMENT.key(), () -> {
            departmentActions.showAllDepartments();
            departmentActions.deleteDepartment();
            return true;
        });
        map.put(MenuOption.CREATE_PRODUCT.key(), () -> {
            departmentActions.showAllDepartments();
            productActions.createProduct();
            return true;
        });
        map.put(MenuOption.UPDATE_PRODUCT.key(), () -> {
            productActions.showAllProducts();
            departmentActions.showAllDepartments();
            productActions.updateProduct();
            return true;
        });
        map.put(MenuOption.DELETE_PRODUCT.key(), () -> {
            productActions.showAllProducts();
            productActions.deleteProduct();
            return true;
        });
        map.put(MenuOption.SHOW_PRODUCTS_BY_DEPARTMENT.key(), () -> {
            departmentActions.showAllDepartments();
            productActions.showProductsByDepartment();
            return true;
        });
        map.put(MenuOption.SHOW_DEPARTMENTS_WITHOUT_PRODUCTS.key(), () -> {
            departmentActions.showDepartmentsWithoutProducts();
            return true;
        });
        map.put(MenuOption.SHOW_ALL_PRODUCTS.key(), () -> {
            productActions.showAllProducts();
            return true;
        });
        map.put(MenuOption.SHOW_ALL_DEPARTMENTS.key(), () -> {
            departmentActions.showAllDepartments();
            return true;
        });
        map.put(MenuOption.EXIT.key(), () -> {
            System.out.println("Выход");
            return false;
        });
        return map;
    }

    private void printMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.key() + " - " + option.label());
        }
    }
}