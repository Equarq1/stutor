<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Лента объявлений">
    <div class="offer-cards">
        <c:forEach var="offer" items="${allOffers}">
            <t:offerCard offer="${offer}"/>
        </c:forEach>
    </div>
</t:layout>