package org.nahap.strore.application.service;

import org.nahap.strore.application.exception.NotFoundException;
import org.nahap.strore.application.exception.ValidationException;
import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.domain.model.Department;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ProductRepository productRepository;

    public DepartmentService(DepartmentRepository departmentRepository, ProductRepository productRepository) {
        this.departmentRepository = Objects.requireNonNull(departmentRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    public Department createDepartment(String name, LocalTime openTime, LocalTime closeTime) {
        validateName(name);
        validateWorkingHours(openTime, closeTime);
        Department department = new Department(0, name.trim(), openTime, closeTime);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(int id, String name, LocalTime openTime, LocalTime closeTime) {
        validateId(id, "Некорректный id отдела");
        validateName(name);
        validateWorkingHours(openTime, closeTime);
        Department existingDepartment = findDepartment(id);
        Department updated = existingDepartment.withNameAndWorkingHours(name.trim(), openTime, closeTime);
        return departmentRepository.save(updated);
    }

    public void deleteDepartment(int id) {
        validateId(id, "Некорректный id отдела");
        findDepartment(id);
        productRepository.deleteByDepartmentId(id);
        departmentRepository.deleteById(id);
    }

    public Department getDepartmentById(int id) {
        validateId(id, "Некорректный id отдела");
        return findDepartment(id);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<Department> getDepartmentsWithoutProducts() {
        return departmentRepository.findAll().stream()
                .filter(department -> !productRepository.existsByDepartmentId(department.getId()))
                .collect(Collectors.toList());
    }

    private Department findDepartment(int id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Отдел с id " + id + " не найден"));
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Название отдела не может быть пустым");
        }
    }

    private void validateWorkingHours(LocalTime openTime, LocalTime closeTime) {
        if (openTime == null) {
            throw new ValidationException("Время открытия не может быть пустым");
        }
        if (closeTime == null) {
            throw new ValidationException("Время закрытия не может быть пустым");
        }
        if (openTime.compareTo(closeTime) >= 0) {
            throw new ValidationException("Время открытия должно быть раньше времени закрытия");
        }
    }

    private void validateId(int id, String message) {
        if (id <= 0) {
            throw new ValidationException(message);
        }
    }
}