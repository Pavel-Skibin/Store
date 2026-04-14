package org.nahap.strore.infrastructure.jdbc.script;

import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlScriptRunner {
    private final JdbcConnectionProvider connectionProvider;

    public SqlScriptRunner(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "JdbcConnectionProvider cannot be null");
    }

    public void runScript(String classpathLocation) {
        String sql = loadScript(classpathLocation);
        String[] statements = sql.split(";");

        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement()) {
            for (String part : statements) {
                String normalized = part.trim();
                if (!normalized.isEmpty()) {
                    statement.execute(normalized);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to execute script: " + classpathLocation, ex);
        }
    }

    public void runScriptIfTableEmpty(String tableName, String classpathLocation) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            long count = rs.getLong(1);
            if (count == 0) {
                runScript(classpathLocation);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to check table content: " + tableName, ex);
        }
    }

    private String loadScript(String classpathLocation) {
        InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(classpathLocation);
        if (input == null) {
            throw new IllegalStateException("SQL script not found: " + classpathLocation);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read SQL script: " + classpathLocation, ex);
        }
    }
}