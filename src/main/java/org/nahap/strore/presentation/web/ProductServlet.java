package org.nahap.strore.presentation.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nahap.strore.application.exception.NotFoundException;
import org.nahap.strore.application.exception.ValidationException;
import org.nahap.strore.domain.model.Department;
import org.nahap.strore.domain.model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {
        "/products",
        "/products/new",
        "/products/edit",
        "/products/save",
        "/products/delete"
})
public class ProductServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/products":
                showProducts(request, response);
                break;
            case "/products/new":
                showProductForm(request, response, null);
                break;
            case "/products/edit":
                showProductEdit(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/products/save":
                saveProduct(request, response);
                break;
            case "/products/delete":
                deleteProduct(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void showProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService().getAllProducts();
        request.setAttribute(WebKeys.PRODUCTS, products);
        forward(request, response, "/WEB-INF/views/products.jsp");
    }

    private void showProductForm(HttpServletRequest request, HttpServletResponse response, Product product)
            throws ServletException, IOException {
        List<Department> departments = departmentService().getAllDepartments();
        request.setAttribute(WebKeys.DEPARTMENTS, departments);
        request.setAttribute(WebKeys.PRODUCT, product);
        forward(request, response, "/WEB-INF/views/product-form.jsp");
    }

    private void showProductEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = parseId(request, "id");
            Product product = productService().getProductById(id);
            showProductForm(request, response, product);
        } catch (ValidationException | NotFoundException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            showProducts(request, response);
        }
    }

    private void saveProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idValue = request.getParameter("id");
        String departmentIdValue = request.getParameter("departmentId");
        String name = request.getParameter("name");
        String priceValue = request.getParameter("price");

        try {
            int departmentId = parseIdValue(departmentIdValue, "Некорректный id отдела");
            BigDecimal price = parsePrice(priceValue);
            if (idValue == null || idValue.isBlank()) {
                productService().createProduct(departmentId, name, price);
            } else {
                int id = parseIdValue(idValue, "Некорректный id товара");
                productService().updateProduct(id, departmentId, name, price);
            }
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (ValidationException | NotFoundException | NumberFormatException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            Product product = buildProduct(idValue, departmentIdValue, name, priceValue);
            showProductForm(request, response, product);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = parseId(request, "id");
            productService().deleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (ValidationException | NotFoundException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            showProducts(request, response);
        }
    }

    private int parseId(HttpServletRequest request, String param) {
        String idValue = request.getParameter(param);
        if (idValue == null || idValue.isBlank()) {
            throw new ValidationException("Некорректный id");
        }
        return Integer.parseInt(idValue.trim());
    }

    private int parseIdValue(String idValue, String message) {
        if (idValue == null || idValue.isBlank()) {
            throw new ValidationException(message);
        }
        return Integer.parseInt(idValue.trim());
    }

    private BigDecimal parsePrice(String priceValue) {
        if (priceValue == null || priceValue.isBlank()) {
            throw new ValidationException("Цена не может быть пустой");
        }
        return new BigDecimal(priceValue.trim());
    }

    private Product buildProduct(String idValue, String departmentIdValue, String name, String priceValue) {
        int id = 0;
        int departmentId = 0;
        BigDecimal price = null;
        if (idValue != null && !idValue.isBlank()) {
            try {
                id = Integer.parseInt(idValue.trim());
            } catch (NumberFormatException ignored) {
                id = 0;
            }
        }
        if (departmentIdValue != null && !departmentIdValue.isBlank()) {
            try {
                departmentId = Integer.parseInt(departmentIdValue.trim());
            } catch (NumberFormatException ignored) {
                departmentId = 0;
            }
        }
        if (priceValue != null && !priceValue.isBlank()) {
            try {
                price = new BigDecimal(priceValue.trim());
            } catch (NumberFormatException ignored) {
                price = null;
            }
        }
        return new Product(id, departmentId, name == null ? "" : name, price);
    }
}
