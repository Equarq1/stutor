<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>
<main>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <h2>Последние объявления</h2>
            <div class="offer-cards">
                <c:forEach var="offer" items="${lastOffers}">
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
        </c:when>

        <c:otherwise>
            <h1>Сайт поможет найти репетитора</h1>
            <h2>Зарегистрируйтесь, чтобы смотреть и создавать объявления</h2>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>