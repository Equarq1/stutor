<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Предметы">
    <main>
        <h2>Панель администратора: Предметы</h2>
        <jsp:include page="/jsp/admin-nav.jsp"/>

        <c:if test="${not empty errorMessage}">
            <div class="error-message"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/subjects" method="post">
            <input type="hidden" name="action" value="add">
            <input type="text" name="name" placeholder="Название предмета" maxlength="200" required>

            <select name="facultyId" required>
                <option value="" disabled selected>Выберите институт...</option>
                <c:forEach var="faculty" items="${faculties}">
                    <option value="${faculty.id}"><c:out value="${faculty.name}"/></option>
                </c:forEach>
            </select>

            <select name="course" required>
                <option value="" disabled selected>Выберите курс...</option>
                <option value="1">1 курс</option>
                <option value="2">2 курс</option>
                <option value="3">3 курс</option>
                <option value="4">4 курс</option>
            </select>

            <button type="submit">Добавить предмет</button>
        </form>

        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Предмет</th>
                <th>Институт</th>
                <th>Курс</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="subject" items="${subjects}">
                <tr>
                    <td>${subject.id}</td>
                    <td><c:out value="${subject.name}"/></td>
                    <td><c:out value="${subject.faculty.name}"/></td>
                    <td>${subject.course}</td>
                    <td>
                        <form class="inline-form" action="${pageContext.request.contextPath}/admin/subjects" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="subjectId" value="${subject.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Удалить предмет?');">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>