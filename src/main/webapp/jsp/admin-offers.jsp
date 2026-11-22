<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Объявления">
    <main>
        <h2>Панель администратора: Объявления</h2>

        <jsp:include page="/jsp/admin-nav.jsp"/>

        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Заголовок</th>
                <th>Автор</th>
                <th>Цена</th>
                <th>Статус</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="offer" items="${offers}">
                <tr>
                    <td>${offer.id}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/offer?id=${offer.id}">
                            <c:out value="${offer.title}"/>
                        </a>
                    </td>
                    <td><c:out value="${offer.author.fullName}"/></td>
                    <td>${offer.price} руб.</td>
                    <td>${offer.status}</td>
                    <td>
                        <form class="inline-form" action="${pageContext.request.contextPath}/admin/offers" method="post">
                            <input type="hidden" name="offerId" value="${offer.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Удалить объявление?');">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>