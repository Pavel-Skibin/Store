package org.nahap.strore.infrastructure.jdbc.mapper;

import org.nahap.strore.domain.model.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public final class DepartmentRowMapper {
    public Department map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        LocalTime openTime = rs.getTime("open_time").toLocalTime();
        LocalTime closeTime = rs.getTime("close_time").toLocalTime();
        return new Department(id, name, openTime, closeTime);
    }
}
