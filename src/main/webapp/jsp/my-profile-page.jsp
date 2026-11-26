<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Мой профиль">
    <div class="profile-container">
        <div class="profile-header">
            <c:choose>
                <c:when test="${not empty user.avatar}">
                    <img src="${pageContext.request.contextPath}/file?id=${user.avatar.id}" width="100" height="100">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="100" height="100">
                </c:otherwise>
            </c:choose>
            <div class="profile-title">
                <h2><c:out value="${user.fullName}"/></h2>
                <p><c:out value="${user.email}"/></p>
            </div>
        </div>

        <div class="profile-info">
            <p><strong>Город:</strong> <c:out value="${not empty user.city ? user.city.name : 'Не указан'}"/></p>
            <p><strong>Факультет:</strong> <c:out value="${not empty user.group ? user.group.faculty.name : 'Не указан'}"/></p>
            <p><strong>Группа:</strong> <c:out value="${not empty user.group ? user.group.name : 'Не указана'}"/></p>
        </div>

        <hr class="profile-divider">
        <h3>О себе</h3>
        <p><c:out value="${not empty user.about ? user.about : 'Вы еще ничего не рассказали о себе.'}"/></p>

        <h3>Контакты</h3>
        <p><c:out value="${not empty user.contact ? user.contact : 'Контакты не указаны.'}"/></p>

        <a href="${pageContext.request.contextPath}/profile/update" class="button">Редактировать профиль</a>

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