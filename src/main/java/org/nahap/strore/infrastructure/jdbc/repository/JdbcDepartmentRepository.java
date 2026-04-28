package org.nahap.strore.infrastructure.jdbc.repository;

import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.domain.model.Department;
import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;
import org.nahap.strore.infrastructure.jdbc.mapper.DepartmentRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcDepartmentRepository implements DepartmentRepository {
    private static final String INSERT_SQL = "INSERT INTO department(name, open_time, close_time) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE department SET name = ?, open_time = ?, close_time = ? WHERE id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, open_time, close_time FROM department WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, name, open_time, close_time FROM department ORDER BY id";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM department WHERE id = ?";
    private static final String EXISTS_BY_ID_SQL = "SELECT 1 FROM department WHERE id = ?";

    private final JdbcConnectionProvider connectionProvider;
    private final DepartmentRowMapper rowMapper;

    public JdbcDepartmentRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "JdbcConnectionProvider cannot be null");
        this.rowMapper = new DepartmentRowMapper();
    }

    @Override
    public Department save(Department department) {
        if (department.getId() <= 0) {
            return insert(department);
        }
        return update(department);
    }

    @Override
    public Optional<Department> findById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(rowMapper.map(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find department by id: " + id, ex);
        }
    }

    @Override
    public List<Department> findAll() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = statement.executeQuery()) {
            List<Department> departments = new ArrayList<>();
            while (rs.next()) {
                departments.add(rowMapper.map(rs));
            }
            return departments;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find all departments", ex);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to delete department with id: " + id, ex);
        }
    }

    @Override
    public boolean existsById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to check department existence for id: " + id, ex);
        }
    }

    private Department insert(Department department) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, department.getName());
            statement.setTime(2, Time.valueOf(department.getOpenTime()));
            statement.setTime(3, Time.valueOf(department.getCloseTime()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new IllegalStateException("No generated key for department");
                }
                int id = keys.getInt(1);
                return new Department(id, department.getName(), department.getOpenTime(), department.getCloseTime());
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to insert department", ex);
        }
    }

    private Department update(Department department) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, department.getName());
            statement.setTime(2, Time.valueOf(department.getOpenTime()));
            statement.setTime(3, Time.valueOf(department.getCloseTime()));
            statement.setInt(4, department.getId());
            statement.executeUpdate();
            return department;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to update department with id: " + department.getId(), ex);
        }
    }
}
