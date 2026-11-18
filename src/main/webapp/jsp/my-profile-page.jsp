<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Мой профиль</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<div class="profile-container">
    <div class="profile-header">
        <c:choose>
            <c:when test="${not empty user.avatar}">
                <img src="${pageContext.request.contextPath}/file?id=${user.avatar.id}" width="100" height="100">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="100" height="100">
            </c:otherwise>
        </c:choose>
        <div class="profile-title">
            <h2>${user.fullName}</h2>
            <p>${user.email}</p>
        </div>
    </div>

    <div class="profile-info">
        <p><strong>Город:</strong> ${not empty user.city ? user.city.name : 'Не указан'}</p>
        <p><strong>Факультет:</strong> ${not empty user.group ? user.group.faculty.name : 'Не указан'}</p>
        <p><strong>Группа:</strong> ${not empty user.group ? user.group.name : 'Не указана'}</p>
    </div>

    <hr class="profile-divider">
    <h3>О себе</h3>
    <p>${not empty user.about ? user.about : 'Вы еще ничего не рассказали о себе.'}</p>

    <h3>Контакты</h3>
    <p>${not empty user.contact ? user.contact : 'Контакты не указаны.'}</p>

    <a href="${pageContext.request.contextPath}/profile/update" class="button">Редактировать профиль</a>
</div>
</body>
</html>