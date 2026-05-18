package org.nahap.strore.presentation.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected DepartmentService departmentService() {
        return (DepartmentService) getServletContext().getAttribute(WebKeys.DEPARTMENT_SERVICE);
    }

    protected ProductService productService() {
        return (ProductService) getServletContext().getAttribute(WebKeys.PRODUCT_SERVICE);
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
