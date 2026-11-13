<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <title>Document</title>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>
<div class="offer-cards">
    <c:forEach var="offer" items="${allOffers}">
        <div class="card">
            <h3>${offer.title}</h3>
            <p class="category">${offer.category.name}</p>
            <p class="author">Преподаватель: ${offer.author.fullName}</p>

            <p><strong>Цена:</strong> ${offer.price} руб.</p>
            <p><strong>Формат:</strong> ${offer.format}</p>

            <a href="${pageContext.request.contextPath}/offer?id=${offer.id}">Подробнее</a>
        </div>
    </c:forEach>
</div>
</body>
</html>