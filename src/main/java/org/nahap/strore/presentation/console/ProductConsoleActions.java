package org.nahap.strore.presentation.console;

import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.domain.model.Product;

public class ProductConsoleActions {
    private final ProductService productService;
    private final ConsoleInput input;

    public ProductConsoleActions(ProductService productService, ConsoleInput input) {
        this.productService = productService;
        this.input = input;
    }

    public void createProduct() {
        int departmentId = input.readInt("Id отдела: ");
        String name = input.readText("Название товара: ");
        Product product = productService.createProduct(departmentId, name, input.readPrice("Цена товара: "));
        System.out.println("Товар добавлен: " + formatProduct(product));
    }

    public void updateProduct() {
        int id = input.readInt("Id товара: ");
        int departmentId = input.readInt("Новый id отдела: ");
        String name = input.readText("Новое название: ");
        Product product = productService.updateProduct(id, departmentId, name, input.readPrice("Новая цена: "));
        System.out.println("Товар обновлен: " + formatProduct(product));
    }

    public void deleteProduct() {
        int id = input.readInt("Id товара для удаления: ");
        productService.deleteProduct(id);
        System.out.println("Товар удален");
    }

    public void showProductsByDepartment() {
        int departmentId = input.readInt("Id отдела: ");
        if (productService.getProductsByDepartment(departmentId).isEmpty()) {
            System.out.println("В отделе нет товаров");
            return;
        }
        for (Product product : productService.getProductsByDepartment(departmentId)) {
            System.out.println(formatProduct(product));
        }
    }

    public void showAllProducts() {
        if (productService.getAllProducts().isEmpty()) {
            System.out.println("Товаров нет");
            return;
        }
        for (Product product : productService.getAllProducts()) {
            System.out.println(formatProduct(product));
        }
    }

    private String formatProduct(Product product) {
        return product.getId() + " | " + product.getName() + " | "
                + product.getPrice().toPlainString() + " | отдел " + product.getDepartmentId();
    }
}