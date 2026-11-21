<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Stutor — Главная">
    <main>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <h2>Последние объявления</h2>
                <div class="offer-cards">
                    <c:forEach var="offer" items="${lastOffers}">
                        <t:offerCard offer="${offer}"/>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <h1>Сайт поможет найти репетитора</h1>
                <h2>Зарегистрируйтесь, чтобы смотреть и создавать объявления</h2>
            </c:otherwise>
        </c:choose>
    </main>
</t:layout>