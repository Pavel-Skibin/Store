package org.nahap.strore.infrastructure.jdbc.mapper;

import org.nahap.strore.domain.model.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProductRowMapper {
    public Product map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int departmentId = rs.getInt("department_id");
        String name = rs.getString("name");
        BigDecimal price = rs.getBigDecimal("price");
        return new Product(id, departmentId, name, price);
    }
}
