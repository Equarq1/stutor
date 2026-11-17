<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

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
    <c:choose>
        <c:when test="${not empty userOffers}">
            <c:forEach var="offer" items="${userOffers}">
                <div class="card">
                    <h3>${offer.title}</h3>
                    <p class="category">${offer.category.name}</p>
                    <p class="author">Преподаватель: ${offer.author.fullName}</p>

                    <p><strong>Цена:</strong> ${offer.price} руб.</p>
                    <p><strong>Формат:</strong> ${offer.format}</p>

                    <a href="${pageContext.request.contextPath}/offer?id=${offer.id}">Подробнее</a>
                </div>
                <a href="${pageContext.request.contextPath}/offer/update?id=${offer.id}">Изменить</a>
                <form action="${pageContext.request.contextPath}/offer/status" method="POST">
                    <input type="hidden" name="id" value="${offer.id}">
                    <c:choose>
                        <c:when test="${offer.status == 'ACTIVE'}">
                            <input type="hidden" name="newStatus" value="CLOSED">
                            <button class="change-status-button">Закрыть объявление</button>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="newStatus" value="ACTIVE">
                            <button class="change-status-button">Открыть объявление</button>
                        </c:otherwise>
                    </c:choose>
                </form>

                <form action="${pageContext.request.contextPath}/offer/delete" method="post">
                    <input type="hidden" name="id" value="${offer.id}">
                    <button type="submit" class="delete-button" onclick="return confirm('Точно удалить это объявление?');">Удалить</button>
                </form>

            </c:forEach>
        </c:when>
        <c:otherwise>
            <h1>Вы не выкладывали никакие объявления </h1>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>