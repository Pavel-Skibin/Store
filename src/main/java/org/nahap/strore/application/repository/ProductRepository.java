package org.nahap.strore.application.repository;

import org.nahap.strore.domain.model.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product> {

    List<Product> findByDepartmentId(int departmentId);

    void deleteByDepartmentId(int departmentId);

    boolean existsByDepartmentId(int departmentId);
}