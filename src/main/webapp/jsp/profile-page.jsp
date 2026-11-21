<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Профиль: ${targetUser.fullName}">
    <div class="profile-container">
        <div class="profile-header">
            <c:choose>
                <c:when test="${not empty targetUser.avatar}">
                    <img src="${pageContext.request.contextPath}/file?id=${targetUser.avatar.id}" width="100" height="100">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="100" height="100">
                </c:otherwise>
            </c:choose>
            <div class="profile-title">
                <h2>${targetUser.fullName}</h2>
            </div>
        </div>

        <div class="profile-info">
            <p><strong>Институт:</strong> ${not empty targetUser.group ? targetUser.group.faculty.name : 'Не указан'}</p>
        </div>

        <hr class="profile-divider">

        <h3>О себе</h3>
        <p>${not empty targetUser.about ? targetUser.about : 'Пользователь пока не добавил информацию о себе.'}</p>

        <div class="contacts-placeholder">
            <p><em>Контакты будут доступны после того, как преподаватель примет вашу заявку на обучение.</em></p>
        </div>

        <hr class="profile-divider">

        <h3>
            Отзывы
            <c:if test="${not empty avgRating}">
                <span class="avg-rating">★ ${avgRating} / 5</span>
            </c:if>
        </h3>

        <c:choose>
            <c:when test="${not empty reviews}">
                <div class="reviews-list">
                    <c:forEach var="review" items="${reviews}">
                        <div class="review-card">
                            <div class="review-header">
                                <strong><c:out value="${review.student.fullName}"/></strong>
                                <span class="review-rating">★ ${review.rating} / 5</span>
                            </div>
                            <c:if test="${not empty review.comment}">
                                <p class="review-comment"><c:out value="${review.comment}"/></p>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="placeholder-text">Отзывов пока нет.</p>
            </c:otherwise>
        </c:choose>
    </div>
</t:layout>