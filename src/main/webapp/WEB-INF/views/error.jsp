<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@ page
import="org.nahap.strore.presentation.web.WebKeys" %> <%
request.setAttribute("pageTitle", "Ошибка"); %> <%@ include
file="/WEB-INF/views/partials/header.jspf" %>
<h1>Ошибка</h1>
<jsp:include page="/WEB-INF/views/partials/menu.jsp" />
<p style="color: red"><%= request.getAttribute(WebKeys.ERROR_MESSAGE) %></p>
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />
