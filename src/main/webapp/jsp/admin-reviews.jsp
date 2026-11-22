<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Отзывы">
    <main>
        <h2>Панель администратора: Отзывы</h2>
        <jsp:include page="/jsp/admin-nav.jsp"/>

        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Репетитор</th>
                <th>Ученик</th>
                <th>Оценка</th>
                <th>Комментарий</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="review" items="${reviews}">
                <tr>
                    <td>${review.id}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/user?id=${review.tutor.id}">
                            <c:out value="${review.tutor.fullName}"/>
                        </a>
                    </td>
                    <td><c:out value="${review.student.fullName}"/></td>
                    <td>★ ${review.rating} / 5</td>
                    <td><c:out value="${review.comment}"/></td>
                    <td>
                        <form class="inline-form" action="${pageContext.request.contextPath}/admin/reviews" method="post">
                            <input type="hidden" name="reviewId" value="${review.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Удалить отзыв?');">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>