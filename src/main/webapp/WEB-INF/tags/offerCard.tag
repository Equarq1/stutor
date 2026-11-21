<%@ tag description="Карточка объявления" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="offer" required="true" type="org.example.models.Offer" %>

<div class="card">
    <h3><c:out value="${offer.title}"/></h3>
    <p class="category"><c:out value="${offer.category.name}"/></p>
    <p class="author">Преподаватель: <c:out value="${offer.author.fullName}"/></p>
    <p><strong>Цена:</strong> ${offer.price} руб.</p>
    <p><strong>Формат:</strong> ${offer.format}</p>
    <a href="${pageContext.request.contextPath}/offer?id=${offer.id}">Подробнее</a>
</div>