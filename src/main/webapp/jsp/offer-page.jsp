<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="${offer.title}">
  <div class="offer-page">
    <h1><c:out value="${offer.title}"/></h1>
    <p class="badge"><c:out value="${offer.category.name}"/></p>

    <div class="author-info">
      <c:choose>
        <c:when test="${not empty offer.author.avatar}">
          <img src="${pageContext.request.contextPath}/file?id=${offer.author.avatar.id}" width="50" height="50">
        </c:when>
        <c:otherwise>
          <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="50" height="50">
        </c:otherwise>
      </c:choose>
      <p><strong>Репетитор:</strong>
        <a href="${pageContext.request.contextPath}/user?id=${offer.author.id}">
          <c:out value="${offer.author.fullName}"/>
        </a>
      </p>
    </div>

    <hr>

    <div class="offer-description">
      <h3>О занятии:</h3>
      <p><c:out value="${offer.description}"/></p>
    </div>

    <div class="offer-details">
      <p><strong>Стоимость:</strong> ${offer.price} руб.</p>
      <p><strong>Формат:</strong> ${offer.format}</p>
    </div>

    <c:if test="${not empty offer.subjects}">
      <div class="offer-subjects">
        <h3>Дисциплины:</h3>
        <ul>
          <c:forEach var="subject" items="${offer.subjects}">
            <li><c:out value="${subject.name}"/> (Курс: ${subject.course}, Факультет: <c:out value="${subject.faculty.name}"/>)</li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <c:if test="${not empty offer.attachment}">
      <div class="offer-attachment">
        <h3>Прикрепленные материалы:</h3>
        <a href="${pageContext.request.contextPath}/file?id=${offer.attachment.id}">Скачать <c:out value="${offer.attachment.originalFileName}"/></a>
      </div>
    </c:if>

    <c:if test="${not empty user and offer.author.id != user.id and offer.status == 'ACTIVE'}">
      <a href="${pageContext.request.contextPath}/request/create?offerId=${offer.id}" class="button">Откликнуться на объявление</a>
    </c:if>
  </div>
</t:layout>