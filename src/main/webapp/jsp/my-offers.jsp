<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Мои объявления">
    <main>
        <c:choose>
            <c:when test="${not empty userOffers}">
                <div class="offer-cards">
                    <c:forEach var="offer" items="${userOffers}">
                        <div class="card">
                            <h3><c:out value="${offer.title}"/></h3>
                            <p class="category"><c:out value="${offer.category.name}"/></p>
                            <p><strong>Цена:</strong> ${offer.price} руб.</p>
                            <p><strong>Формат:</strong> ${offer.format}</p>
                            <p><strong>Статус:</strong> ${offer.status}</p>

                            <a href="${pageContext.request.contextPath}/offer?id=${offer.id}">Подробнее</a>
                            <a href="${pageContext.request.contextPath}/offer/update?id=${offer.id}">Изменить</a>

                            <div class="card-actions">
                                <form action="${pageContext.request.contextPath}/offer/status" method="post">
                                    <input type="hidden" name="id" value="${offer.id}">
                                    <c:choose>
                                        <c:when test="${offer.status == 'ACTIVE'}">
                                            <input type="hidden" name="newStatus" value="CLOSED">
                                            <button type="submit" class="change-status-button">Закрыть</button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="newStatus" value="ACTIVE">
                                            <button type="submit" class="change-status-button">Открыть</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>

                                <form action="${pageContext.request.contextPath}/offer/delete" method="post">
                                    <input type="hidden" name="id" value="${offer.id}">
                                    <button type="submit" class="delete-button"
                                            onclick="return confirm('Точно удалить это объявление?');">Удалить</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <h2>Вы не выкладывали никакие объявления</h2>
            </c:otherwise>
        </c:choose>
    </main>
</t:layout>