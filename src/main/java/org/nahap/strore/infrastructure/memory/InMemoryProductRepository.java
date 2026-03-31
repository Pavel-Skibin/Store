package org.nahap.strore.infrastructure.memory;

import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.domain.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public Product save(Product product) {
        int id = product.getId();
        if (id <= 0) {
            id = nextId++;
        }
        Product copy = new Product(id, product.getDepartmentId(), product.getName(), new BigDecimal(product.getPrice().toPlainString()));
        products.put(id, copy);
        return copy;
    }

    @Override
    public Optional<Product> findById(int id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> findByDepartmentId(int departmentId) {
        return products.values().stream()
                .filter(product -> product.getDepartmentId() == departmentId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        products.remove(id);
    }

    @Override
    public void deleteByDepartmentId(int departmentId) {
        products.entrySet().removeIf(entry -> entry.getValue().getDepartmentId() == departmentId);
    }

    @Override
    public boolean existsByDepartmentId(int departmentId) {
        return products.values().stream().anyMatch(product -> product.getDepartmentId() == departmentId);
    }
}