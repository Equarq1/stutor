<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Города">
    <main>
        <h2>Панель администратора: Города</h2>

        <jsp:include page="/jsp/admin-nav.jsp"/>

        <c:if test="${not empty errorMessage}">
            <div class="error-message"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/cities" method="post">
            <input type="hidden" name="action" value="add">
            <input type="text" name="name" placeholder="Название города" maxlength="100" required>
            <button type="submit">Добавить город</button>
        </form>

        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="city" items="${cities}">
                <tr>
                    <td>${city.id}</td>
                    <td><c:out value="${city.name}"/></td>
                    <td>
                        <form class="inline-form" action="${pageContext.request.contextPath}/admin/cities" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="cityId" value="${city.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Удалить город?');">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>