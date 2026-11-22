<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header>
    <div class="logo">
        <a href="${pageContext.request.contextPath}/home">Stutor</a>
    </div>

    <c:choose>
        <c:when test="${not empty sessionScope.user}">

            <nav class="menu">
                <a href="${pageContext.request.contextPath}/offers">Лента объявлений</a>
                <a href="${pageContext.request.contextPath}/my-offers">Мои объявления</a>
                <a href="${pageContext.request.contextPath}/create-offer">Создать объявление</a>
                <a href="${pageContext.request.contextPath}/requests">Заявки</a>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/users">Админка</a>
                </c:if>
            </nav>

            <div class="auth-menu">
                <a href="${pageContext.request.contextPath}/profile">Профиль</a>
                <a href="${pageContext.request.contextPath}/logout">Выйти</a>
            </div>

        </c:when>

        <c:otherwise>
            <div class="auth-menu">
                <a href="${pageContext.request.contextPath}/login">Войти</a>
                <a href="${pageContext.request.contextPath}/registration">Зарегистрироваться</a>
            </div>
        </c:otherwise>
    </c:choose>
</header>