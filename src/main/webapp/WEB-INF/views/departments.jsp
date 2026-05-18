<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.nahap.strore.domain.model.Department" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    request.setAttribute("pageTitle", "Отделы");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Отделы</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p><a href="<%= request.getContextPath() %>/departments/new">Добавить отдел</a></p>
<%
    String error = (String) request.getAttribute(WebKeys.ERROR_MESSAGE);
    if (error != null && !error.isBlank()) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
    List<Department> departments = (List<Department>) request.getAttribute(WebKeys.DEPARTMENTS);
    if (departments == null || departments.isEmpty()) {
%>
<p>Отделов нет.</p>
<%
    } else {
%>
<table border="1" cellpadding="6" cellspacing="0">
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Открытие</th>
        <th>Закрытие</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Department department : departments) {
    %>
    <tr>
        <td><%= department.getId() %></td>
        <td><%= department.getName() %></td>
        <td><%= department.getOpenTime() %></td>
        <td><%= department.getCloseTime() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/departments/edit?id=<%= department.getId() %>">Редактировать</a>
            <form method="post" action="<%= request.getContextPath() %>/departments/delete" style="display:inline;">
                <input type="hidden" name="id" value="<%= department.getId() %>">
                <button type="submit">Удалить</button>
            </form>
            <a href="<%= request.getContextPath() %>/departments/products?id=<%= department.getId() %>">Товары</a>
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
