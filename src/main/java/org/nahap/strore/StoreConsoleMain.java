package org.nahap.strore;

import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.infrastructure.jdbc.config.DatabaseConfig;
import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;
import org.nahap.strore.infrastructure.jdbc.repository.JdbcDepartmentRepository;
import org.nahap.strore.infrastructure.jdbc.repository.JdbcProductRepository;
import org.nahap.strore.infrastructure.jdbc.script.SqlScriptRunner;
import org.nahap.strore.presentation.console.ConsoleMenu;

public final class StoreConsoleMain {
    private StoreConsoleMain() {
    }

    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();
        JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider(config);
        SqlScriptRunner scriptRunner = new SqlScriptRunner(connectionProvider);
        scriptRunner.runScript("sql/schema.sql");
        scriptRunner.runScriptIfTableEmpty("department", "sql/seed.sql");

        DepartmentRepository departmentRepository = new JdbcDepartmentRepository(connectionProvider);
        ProductRepository productRepository = new JdbcProductRepository(connectionProvider);
        DepartmentService departmentService = new DepartmentService(departmentRepository, productRepository);
        ProductService productService = new ProductService(productRepository, departmentRepository);
        ConsoleMenu consoleMenu = new ConsoleMenu(departmentService, productService);
        consoleMenu.start();
    }
}