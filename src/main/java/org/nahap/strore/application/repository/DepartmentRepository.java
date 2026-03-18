package org.nahap.strore.application.repository;

import org.nahap.strore.domain.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {
    Department save(Department department);

    Optional<Department> findById(int id);

    List<Department> findAll();

    void deleteById(int id);

    boolean existsById(int id);
}