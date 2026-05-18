<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.nahap.strore.domain.model.Department" %>
<%@ page import="org.nahap.strore.domain.model.Product" %>
<%@ page import="org.nahap.strore.presentation.web.WebKeys" %>
<%
    Product product = (Product) request.getAttribute(WebKeys.PRODUCT);
    List<Department> departments = (List<Department>) request.getAttribute(WebKeys.DEPARTMENTS);
    String error = (String) request.getAttribute(WebKeys.ERROR_MESSAGE);
    String idValue = product != null ? String.valueOf(product.getId()) : "";
    String nameValue = product != null ? product.getName() : "";
    String priceValue = product != null && product.getPrice() != null ? product.getPrice().toString() : "";
    int selectedDepartmentId = product != null ? product.getDepartmentId() : 0;
    request.setAttribute("pageTitle", "Товар");
%>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>
<h1>Товар</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p><a href="<%= request.getContextPath() %>/products">Назад к товарам</a></p>
<%
    if (error != null && !error.isBlank()) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
%>
<form method="post" action="<%= request.getContextPath() %>/products/save">
    <% if (!idValue.isBlank()) { %>
    <input type="hidden" name="id" value="<%= idValue %>">
    <% } %>
    <div>
        <label>Отдел:</label>
        <select name="departmentId" required>
            <option value="">Выберите отдел</option>
            <%
                if (departments != null) {
                    for (Department department : departments) {
                        String selected = department.getId() == selectedDepartmentId ? "selected" : "";
            %>
            <option value="<%= department.getId() %>" <%= selected %>><%= department.getName() %></option>
            <%
                    }
                }
            %>
        </select>
    </div>
    <div>
        <label>Название:</label>
        <input type="text" name="name" value="<%= nameValue %>" required>
    </div>
    <div>
        <label>Цена:</label>
        <input type="number" step="0.01" name="price" value="<%= priceValue %>" required>
    </div>
    <button type="submit">Сохранить</button>
</form>
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
