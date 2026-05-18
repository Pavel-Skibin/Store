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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/departments",
        "/departments/new",
        "/departments/edit",
        "/departments/save",
        "/departments/delete",
        "/departments/without-products",
        "/departments/products"
})
public class DepartmentServlet extends BaseServlet {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/departments":
                showDepartments(request, response);
                break;
            case "/departments/new":
                showDepartmentForm(request, response, null);
                break;
            case "/departments/edit":
                showDepartmentEdit(request, response);
                break;
            case "/departments/without-products":
                showDepartmentsWithoutProducts(request, response);
                break;
            case "/departments/products":
                showProductsByDepartment(request, response);
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
            case "/departments/save":
                saveDepartment(request, response);
                break;
            case "/departments/delete":
                deleteDepartment(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void showDepartments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = departmentService().getAllDepartments();
        request.setAttribute(WebKeys.DEPARTMENTS, departments);
        forward(request, response, "/WEB-INF/views/departments.jsp");
    }

    private void showDepartmentsWithoutProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = departmentService().getDepartmentsWithoutProducts();
        request.setAttribute(WebKeys.DEPARTMENTS, departments);
        forward(request, response, "/WEB-INF/views/departments-without-products.jsp");
    }

    private void showDepartmentForm(HttpServletRequest request, HttpServletResponse response, Department department)
            throws ServletException, IOException {
        request.setAttribute(WebKeys.DEPARTMENT, department);
        forward(request, response, "/WEB-INF/views/department-form.jsp");
    }

    private void showDepartmentEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = parseId(request);
            Department department = departmentService().getDepartmentById(id);
            showDepartmentForm(request, response, department);
        } catch (ValidationException | NotFoundException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            showDepartments(request, response);
        }
    }

    private void showProductsByDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = parseId(request);
            Department department = departmentService().getDepartmentById(id);
            List<Product> products = productService().getProductsByDepartment(id);
            request.setAttribute(WebKeys.DEPARTMENT, department);
            request.setAttribute(WebKeys.PRODUCTS, products);
            forward(request, response, "/WEB-INF/views/products-by-department.jsp");
        } catch (ValidationException | NotFoundException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            showDepartments(request, response);
        }
    }

    private void saveDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idValue = request.getParameter("id");
        String name = request.getParameter("name");
        String openTimeValue = request.getParameter("openTime");
        String closeTimeValue = request.getParameter("closeTime");

        try {
            LocalTime openTime = parseTime(openTimeValue);
            LocalTime closeTime = parseTime(closeTimeValue);
            if (idValue == null || idValue.isBlank()) {
                departmentService().createDepartment(name, openTime, closeTime);
            } else {
                int id = Integer.parseInt(idValue.trim());
                departmentService().updateDepartment(id, name, openTime, closeTime);
            }
            response.sendRedirect(request.getContextPath() + "/departments");
        } catch (ValidationException | NotFoundException | DateTimeParseException | NumberFormatException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            Department department = buildDepartment(idValue, name, openTimeValue, closeTimeValue);
            showDepartmentForm(request, response, department);
        }
    }

    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = parseId(request);
            departmentService().deleteDepartment(id);
            response.sendRedirect(request.getContextPath() + "/departments");
        } catch (ValidationException | NotFoundException ex) {
            request.setAttribute(WebKeys.ERROR_MESSAGE, ex.getMessage());
            showDepartments(request, response);
        }
    }

    private int parseId(HttpServletRequest request) {
        String idValue = request.getParameter("id");
        if (idValue == null || idValue.isBlank()) {
            throw new ValidationException("Некорректный id отдела");
        }
        return Integer.parseInt(idValue.trim());
    }

    private LocalTime parseTime(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Время не может быть пустым");
        }
        return LocalTime.parse(value.trim(), TIME_FORMATTER);
    }

    private Department buildDepartment(String idValue, String name, String openTimeValue, String closeTimeValue) {
        int id = 0;
        if (idValue != null && !idValue.isBlank()) {
            try {
                id = Integer.parseInt(idValue.trim());
            } catch (NumberFormatException ignored) {
                id = 0;
            }
        }
        LocalTime openTime = safeParseTime(openTimeValue);
        LocalTime closeTime = safeParseTime(closeTimeValue);
        return new Department(id, name == null ? "" : name, openTime, closeTime);
    }

    private LocalTime safeParseTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(value.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
