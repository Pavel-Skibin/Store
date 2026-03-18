package org.nahap.strore.application.repository;

import org.nahap.strore.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(int id);

    List<Product> findAll();

    List<Product> findByDepartmentId(int departmentId);

    void deleteById(int id);

    void deleteByDepartmentId(int departmentId);

    boolean existsByDepartmentId(int departmentId);
}