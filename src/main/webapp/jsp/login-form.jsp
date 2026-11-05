<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Title</title>
</head>

<body>
<c:if test="${not empty errorMessage}">
    <div>
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
