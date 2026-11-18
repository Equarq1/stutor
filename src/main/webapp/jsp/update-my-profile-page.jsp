<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Редактирование профиля</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<div class="profile-container">
    <h2>Редактирование профиля</h2>
    <form class="profile-form" action="${pageContext.request.contextPath}/profile/update" method="post" enctype="multipart/form-data">

        <label>Немного о себе:</label>
        <textarea name="about" rows="4">${user.about}</textarea>

        <label>Контакты (например, Telegram):</label>
        <input type="text" name="contact" value="${user.contact}">

        <label>Обновить аватар:</label>
        <input type="file" name="avatar" accept="image/*">

        <button type="submit">Сохранить</button>
        <a href="${pageContext.request.contextPath}/profile">Отмена</a>
    </form>
</div>
</body>
</html>