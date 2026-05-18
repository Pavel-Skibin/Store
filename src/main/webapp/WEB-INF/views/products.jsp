<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.nahap.strore.domain.model.Product" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    request.setAttribute("pageTitle", "Товары");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Товары</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p><a href="<%= request.getContextPath() %>/products/new">Добавить товар</a></p>
<%
    String error = (String) request.getAttribute(WebKeys.ERROR_MESSAGE);
    if (error != null && !error.isBlank()) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
    List<Product> products = (List<Product>) request.getAttribute(WebKeys.PRODUCTS);
    if (products == null || products.isEmpty()) {
%>
<p>Товаров нет.</p>
<%
    } else {
%>
<table border="1" cellpadding="6" cellspacing="0">
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Цена</th>
        <th>Отдел</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Product product : products) {
    %>
    <tr>
        <td><%= product.getId() %></td>
        <td><%= product.getName() %></td>
        <td><%= product.getPrice() %></td>
        <td><%= product.getDepartmentId() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/products/edit?id=<%= product.getId() %>">Редактировать</a>
            <form method="post" action="<%= request.getContextPath() %>/products/delete" style="display:inline;">
                <input type="hidden" name="id" value="<%= product.getId() %>">
                <button type="submit">Удалить</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
    }
%>
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
