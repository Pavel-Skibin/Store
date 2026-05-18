<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.nahap.strore.domain.model.Department" %>
<%@ page import="org.nahap.strore.domain.model.Product" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    Department department = (Department) request.getAttribute(WebKeys.DEPARTMENT);
    List<Product> products = (List<Product>) request.getAttribute(WebKeys.PRODUCTS);
    request.setAttribute("pageTitle", "Товары отдела");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Товары отдела <%= department != null ? department.getName() : "" %></h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p><a href="<%= request.getContextPath() %>/departments">Назад к отделам</a></p>
<%
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
