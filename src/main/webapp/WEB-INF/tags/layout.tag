<%@ tag description="Общая разметка страницы" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="headExtra" fragment="true" %>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${title}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <jsp:invoke fragment="headExtra"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>
<jsp:doBody/>
</body>
</html>