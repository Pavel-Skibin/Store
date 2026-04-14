package org.nahap.strore.infrastructure.jdbc.repository;

import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.domain.model.Product;
import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;
import org.nahap.strore.infrastructure.jdbc.mapper.ProductRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcProductRepository implements ProductRepository {
    private static final String INSERT_SQL = "INSERT INTO product(department_id, name, price) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE product SET department_id = ?, name = ?, price = ? WHERE id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT id, department_id, name, price FROM product WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, department_id, name, price FROM product ORDER BY id";
    private static final String FIND_BY_DEPARTMENT_ID_SQL =
            "SELECT id, department_id, name, price FROM product WHERE department_id = ? ORDER BY id";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM product WHERE id = ?";
    private static final String DELETE_BY_DEPARTMENT_ID_SQL = "DELETE FROM product WHERE department_id = ?";
    private static final String EXISTS_BY_DEPARTMENT_ID_SQL = "SELECT 1 FROM product WHERE department_id = ?";

    private final JdbcConnectionProvider connectionProvider;
    private final ProductRowMapper rowMapper;

    public JdbcProductRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "JdbcConnectionProvider cannot be null");
        this.rowMapper = new ProductRowMapper();
    }

    @Override
    public Product save(Product product) {
        if (product.getId() <= 0) {
            return insert(product);
        }
        return update(product);
    }

    @Override
    public Optional<Product> findById(int id) {
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
            throw new IllegalStateException("Failed to find product by id: " + id, ex);
        }
    }

    @Override
    public List<Product> findAll() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = statement.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(rowMapper.map(rs));
            }
            return products;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find all products", ex);
        }
    }

    @Override
    public List<Product> findByDepartmentId(int departmentId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_DEPARTMENT_ID_SQL)) {
            statement.setInt(1, departmentId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(rowMapper.map(rs));
                }
                return products;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find products by department id: " + departmentId, ex);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to delete product with id: " + id, ex);
        }
    }

    @Override
    public void deleteByDepartmentId(int departmentId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_DEPARTMENT_ID_SQL)) {
            statement.setInt(1, departmentId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to delete products by department id: " + departmentId, ex);
        }
    }

    @Override
    public boolean existsByDepartmentId(int departmentId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_DEPARTMENT_ID_SQL)) {
            statement.setInt(1, departmentId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to check product existence for department id: " + departmentId, ex);
        }
    }

    private Product insert(Product product) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, product.getDepartmentId());
            statement.setString(2, product.getName());
            statement.setBigDecimal(3, product.getPrice());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new IllegalStateException("No generated key for product");
                }
                int id = keys.getInt(1);
                return new Product(id, product.getDepartmentId(), product.getName(), product.getPrice());
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to insert product", ex);
        }
    }

    private Product update(Product product) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, product.getDepartmentId());
            statement.setString(2, product.getName());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getId());
            statement.executeUpdate();
            return product;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to update product with id: " + product.getId(), ex);
        }
    }
}