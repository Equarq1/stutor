<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <title>Title</title>
</head>

<body>
<jsp:include page="/jsp/header.jsp"/>
<c:if test="${not empty errorMessage}">
    <div class="error-message">
            ${errorMessage}
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="email" placeholder="Почта КФУ">
    <input type="password" name="password" placeholder="Пароль">
    <button type="submit">Войти</button>
</form>

</body>
</html>
