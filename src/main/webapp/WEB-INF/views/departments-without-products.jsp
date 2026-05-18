<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.nahap.strore.domain.model.Department" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    request.setAttribute("pageTitle", "Отделы без товаров");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Отделы без товаров</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
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
<ul>
    <%
        for (Department department : departments) {
    %>
    <li><%= department.getName() %> (ID: <%= department.getId() %>)</li>
    <%
        }
    %>
</ul>
<%
    }
%>
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
