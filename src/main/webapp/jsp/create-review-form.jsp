<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Оставить отзыв</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<main>
    <h2>Отзыв о репетиторе: <c:out value="${targetRequest.offer.author.fullName}"/></h2>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/review/create" method="post">
        <input type="hidden" name="requestId" value="${targetRequest.id}">

        <label>Оценка:</label>
        <select name="rating" required>
            <option value="5">5 — Отлично</option>
            <option value="4">4 — Хорошо</option>
            <option value="3">3 — Нормально</option>
            <option value="2">2 — Плохо</option>
            <option value="1">1 — Ужасно</option>
        </select>

        <label>Комментарий:</label>
        <textarea name="comment" rows="5" placeholder="Расскажите, как прошли занятия..."></textarea>

        <button type="submit">Отправить отзыв</button>
    </form>
</main>
</body>
</html>