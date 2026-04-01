package org.nahap.strore.application.service;

import org.nahap.strore.application.exception.NotFoundException;
import org.nahap.strore.application.exception.ValidationException;
import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductService {
    private final ProductRepository productRepository;
    private final DepartmentRepository departmentRepository;

    public ProductService(ProductRepository productRepository, DepartmentRepository departmentRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
        this.departmentRepository = Objects.requireNonNull(departmentRepository);
    }

    public Product createProduct(int departmentId, String name, BigDecimal price) {
        validateId(departmentId, "Некорректный id отдела");
        validateName(name);
        validatePrice(price);
        validateDepartmentExists(departmentId);
        Product product = new Product(0, departmentId, name.trim(), price);
        return productRepository.save(product);
    }

    public Product updateProduct(int id, int departmentId, String name, BigDecimal price) {
        validateId(id, "Некорректный id товара");
        validateId(departmentId, "Некорректный id отдела");
        validateName(name);
        validatePrice(price);
        validateDepartmentExists(departmentId);
        Product existingProduct = findProduct(id);
        Product updated = existingProduct.withData(departmentId, name.trim(), price);
        return productRepository.save(updated);
    }

    public void deleteProduct(int id) {
        validateId(id, "Некорректный id товара");
        findProduct(id);
        productRepository.deleteById(id);
    }

    public Product getProductById(int id) {
        validateId(id, "Некорректный id товара");
        return findProduct(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByDepartment(int departmentId) {
        validateId(departmentId, "Некорректный id отдела");
        validateDepartmentExists(departmentId);
        return productRepository.findByDepartmentId(departmentId);
    }

    private Product findProduct(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар с id " + id + " не найден"));
    }

    private void validateDepartmentExists(int departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new NotFoundException("Отдел с id " + departmentId + " не найден");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Название товара не может быть пустым");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ValidationException("Цена не может быть пустой");
        }
        if (price.signum() < 0) {
            throw new ValidationException("Цена не может быть отрицательной");
        }
    }

    private void validateId(int id, String message) {
        if (id <= 0) {
            throw new ValidationException(message);
        }
    }
}