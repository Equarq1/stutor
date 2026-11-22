<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Категории">
    <main>
        <h2>Панель администратора: Категории</h2>
        <jsp:include page="/jsp/admin-nav.jsp"/>

        <c:if test="${not empty errorMessage}">
            <div class="error-message"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/categories" method="post">
            <input type="hidden" name="action" value="add">
            <input type="text" name="name" placeholder="Название категории" maxlength="100" required>
            <button type="submit">Добавить категорию</button>
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
            <c:forEach var="category" items="${categories}">
                <tr>
                    <td>${category.id}</td>
                    <td><c:out value="${category.name}"/></td>
                    <td>
                        <form class="inline-form" action="${pageContext.request.contextPath}/admin/categories" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryId" value="${category.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Удалить категорию?');">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>