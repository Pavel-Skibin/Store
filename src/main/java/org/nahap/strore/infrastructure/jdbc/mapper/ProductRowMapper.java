package org.nahap.strore.infrastructure.jdbc.mapper;

import org.nahap.strore.domain.model.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProductRowMapper {
    private static final String COL_ID = "id";
    private static final String COL_DEPARTMENT_ID = "department_id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";

    public Product map(ResultSet rs) throws SQLException {
        int id = rs.getInt(COL_ID);
        int departmentId = rs.getInt(COL_DEPARTMENT_ID);
        String name = rs.getString(COL_NAME);
        BigDecimal price = rs.getBigDecimal(COL_PRICE);
        return new Product(id, departmentId, name, price);
    }
}
