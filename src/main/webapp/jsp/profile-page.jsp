<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Профиль: ${targetUser.fullName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<div class="profile-container">
    <div class="profile-header">
        <c:choose>
            <c:when test="${not empty targetUser.avatar}">
                <img src="${pageContext.request.contextPath}/file?id=${targetUser.avatar.id}" width="100" height="100">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="100" height="100">
            </c:otherwise>
        </c:choose>
        <div class="profile-title">
            <h2>${targetUser.fullName}</h2>
        </div>
    </div>

    <div class="profile-info">
        <p><strong>Институт:</strong> ${not empty targetUser.group ? targetUser.group.faculty.name : 'Не указан'}</p>
    </div>

    <hr class="profile-divider">

    <h3>О себе</h3>
    <p>${not empty targetUser.about ? targetUser.about : 'Пользователь пока не добавил информацию о себе.'}</p>

    <div class="contacts-placeholder">
        <p><em>Контакты будут доступны после того, как преподаватель примет вашу заявку на обучение.</em></p>
    </div>
</div>
</body>
</html>