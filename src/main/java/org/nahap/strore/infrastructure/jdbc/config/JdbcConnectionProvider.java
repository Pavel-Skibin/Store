package org.nahap.strore.infrastructure.jdbc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class JdbcConnectionProvider {
    private static final String POSTGRES_DRIVER_CLASS = "org.postgresql.Driver";

    private final DatabaseConfig config;

    public JdbcConnectionProvider(DatabaseConfig config) {
        this.config = Objects.requireNonNull(config, "DatabaseConfig cannot be null");
    }

    public Connection getConnection() throws SQLException {
        loadDriver();
        return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    }

    private void loadDriver() {
        try {
            Class.forName(POSTGRES_DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("PostgreSQL JDBC driver is not available on the web application classpath", ex);
        }
    }
}
