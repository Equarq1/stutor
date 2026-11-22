<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Админка — Пользователи">
    <main>
        <h2>Панель администратора: Пользователи</h2>

        <jsp:include page="/jsp/admin-nav.jsp"/>

        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>ФИО</th>
                <th>Email</th>
                <th>Роль</th>
                <th>Статус</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.id}</td>
                    <td><c:out value="${u.fullName}"/></td>
                    <td><c:out value="${u.email}"/></td>
                    <td>${u.role}</td>
                    <td>${u.userStatus}</td>
                    <td>
                        <c:if test="${u.id != sessionScope.user.id}">
                            <form class="inline-form" action="${pageContext.request.contextPath}/admin/users" method="post">
                                <input type="hidden" name="userId" value="${u.id}">
                                <c:choose>
                                    <c:when test="${u.userStatus == 'ACTIVE'}">
                                        <input type="hidden" name="status" value="BLOCKED">
                                        <button type="submit" class="delete-button">Заблокировать</button>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="status" value="ACTIVE">
                                        <button type="submit" class="btn-accept">Разблокировать</button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</t:layout>