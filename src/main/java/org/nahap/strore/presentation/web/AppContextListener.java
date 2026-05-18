package org.nahap.strore.presentation.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.infrastructure.jdbc.config.DatabaseConfig;
import org.nahap.strore.infrastructure.jdbc.config.DatabaseConfigLoader;
import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;
import org.nahap.strore.infrastructure.jdbc.migration.MigrationService;
import org.nahap.strore.infrastructure.jdbc.repository.JdbcDepartmentRepository;
import org.nahap.strore.infrastructure.jdbc.repository.JdbcProductRepository;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfig config = DatabaseConfigLoader.load();
        JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider(config);
        MigrationService migrationService = new MigrationService(connectionProvider);
        migrationService.runMigrations();

        DepartmentRepository departmentRepository = new JdbcDepartmentRepository(connectionProvider);
        ProductRepository productRepository = new JdbcProductRepository(connectionProvider);
        DepartmentService departmentService = new DepartmentService(departmentRepository, productRepository);
        ProductService productService = new ProductService(productRepository, departmentRepository);

        ServletContext context = sce.getServletContext();
        context.setAttribute(WebKeys.DEPARTMENT_SERVICE, departmentService);
        context.setAttribute(WebKeys.PRODUCT_SERVICE, productService);
    }
}
