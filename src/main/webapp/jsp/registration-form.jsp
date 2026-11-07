<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script
            src="https://code.jquery.com/jquery-3.5.1.js"
            integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
            crossorigin="anonymous">
    </script>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>
<c:if test="${not empty errorMessage}">
    <div class="error-message">
            ${errorMessage}
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/registration" method="post">
    <input type="text" name="email" placeholder="Введите почту" required>
    <input type="password" name="password" placeholder="Введите пароль" required>
    <input type="text" name="fullName" placeholder="Введите ФИО" required>
    
    <select id="faculty-select" required>
        <option value="" disabled selected></option>
        <c:forEach items="${faculties}" var="faculty">
            <option value="${faculty.id}">${faculty.name}</option>
        </c:forEach>
    </select>

    <select id="course-select" required>
        <option value="" disabled selected></option>
        <c:forEach begin="1" end="4" var="i">
            <option value="${i}">${i}</option>
        </c:forEach>
    </select>

    <select name="groupId" id="group-select" required>
        <option value="" disabled selected>Сначала выберите факультет и курс...</option>
    </select>

    <select name="cityId" required>
        <option value="" disabled selected></option>
        <c:forEach items="${cities}" var="city">
            <option value="${city.id}">${city.name}</option>
        </c:forEach>
    </select>
    <button type="submit">Зарегестрироваться</button>
</form>

<script>
    const contextPath = '${pageContext.request.contextPath}'
</script>
<script src="${pageContext.request.contextPath}/js/registration-form.js"></script>
</body>
</html>