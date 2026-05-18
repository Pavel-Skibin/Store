<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.nahap.strore.domain.model.Department" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    Department department = (Department) request.getAttribute(WebKeys.DEPARTMENT);
    String error = (String) request.getAttribute(WebKeys.ERROR_MESSAGE);
    String idValue = department != null ? String.valueOf(department.getId()) : "";
    String nameValue = department != null ? department.getName() : "";
    String openTimeValue = department != null && department.getOpenTime() != null
            ? department.getOpenTime().toString() : "";
    String closeTimeValue = department != null && department.getCloseTime() != null
            ? department.getCloseTime().toString() : "";
    request.setAttribute("pageTitle", "Отдел");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Отдел</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p><a href="<%= request.getContextPath() %>/departments">Назад к отделам</a></p>
<%
    if (error != null && !error.isBlank()) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
%>
<form method="post" action="<%= request.getContextPath() %>/departments/save">
    <% if (!idValue.isBlank()) { %>
    <input type="hidden" name="id" value="<%= idValue %>">
    <% } %>
    <div>
        <label>Название:</label>
        <input type="text" name="name" value="<%= nameValue %>" required>
    </div>
    <div>
        <label>Открытие:</label>
        <input type="time" name="openTime" value="<%= openTimeValue %>" required>
    </div>
    <div>
        <label>Закрытие:</label>
        <input type="time" name="closeTime" value="<%= closeTimeValue %>" required>
    </div>
    <button type="submit">Сохранить</button>
</form>
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
