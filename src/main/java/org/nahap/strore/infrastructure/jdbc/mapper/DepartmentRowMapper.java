package org.nahap.strore.infrastructure.jdbc.mapper;

import org.nahap.strore.domain.model.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public final class DepartmentRowMapper {
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_OPEN_TIME = "open_time";
    private static final String COL_CLOSE_TIME = "close_time";

    public Department map(ResultSet rs) throws SQLException {
        int id = rs.getInt(COL_ID);
        String name = rs.getString(COL_NAME);
        LocalTime openTime = rs.getTime(COL_OPEN_TIME).toLocalTime();
        LocalTime closeTime = rs.getTime(COL_CLOSE_TIME).toLocalTime();
        return new Department(id, name, openTime, closeTime);
    }
}
