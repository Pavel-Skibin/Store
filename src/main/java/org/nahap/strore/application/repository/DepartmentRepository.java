package org.nahap.strore.application.repository;

import org.nahap.strore.domain.model.Department;

public interface DepartmentRepository extends Repository<Department> {

    boolean existsById(int id);
}