package org.nahap.strore.presentation.console;

import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.domain.model.Department;

public class DepartmentConsoleActions {
    private final DepartmentService departmentService;
    private final ConsoleInput input;

    public DepartmentConsoleActions(DepartmentService departmentService, ConsoleInput input) {
        this.departmentService = departmentService;
        this.input = input;
    }

    public void createDepartment() {
        String name = input.readText("Название отдела: ");
        Department department = departmentService.createDepartment(
                name,
                input.readLocalTime("Время открытия (HH:mm): "),
                input.readLocalTime("Время закрытия (HH:mm): ")
        );
        System.out.println("Отдел добавлен: " + formatDepartment(department));
    }

    public void updateDepartment() {
        int id = input.readInt("Id отдела: ");
        String name = input.readText("Новое название: ");
        Department department = departmentService.updateDepartment(
                id,
                name,
                input.readLocalTime("Новое время открытия (HH:mm): "),
                input.readLocalTime("Новое время закрытия (HH:mm): ")
        );
        System.out.println("Отдел обновлен: " + formatDepartment(department));
    }

    public void deleteDepartment() {
        int id = input.readInt("Id отдела для удаления: ");
        departmentService.deleteDepartment(id);
        System.out.println("Отдел удален");
    }

    public void showAllDepartments() {
        if (departmentService.getAllDepartments().isEmpty()) {
            System.out.println("Отделов нет");
            return;
        }
        for (Department department : departmentService.getAllDepartments()) {
            System.out.println(formatDepartment(department));
        }
    }

    public void showDepartmentsWithoutProducts() {
        if (departmentService.getDepartmentsWithoutProducts().isEmpty()) {
            System.out.println("Отделов без товаров нет");
            return;
        }
        for (Department department : departmentService.getDepartmentsWithoutProducts()) {
            System.out.println(formatDepartment(department));
        }
    }

    private String formatDepartment(Department department) {
        return department.getId() + " | " + department.getName() + " | "
                + department.getOpenTime().format(input.timeFormatter()) + " - "
                + department.getCloseTime().format(input.timeFormatter());
    }
}