package org.nahap.strore.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {
    private final int id;
    private final int departmentId;
    private final String name;
    private final BigDecimal price;

    public Product(int id, int departmentId, String name, BigDecimal price) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product withData(int newDepartmentId, String newName, BigDecimal newPrice) {
        return new Product(id, newDepartmentId, newName, newPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return id == product.id
                && departmentId == product.departmentId
                && Objects.equals(name, product.name)
                && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentId, name, price);
    }
}