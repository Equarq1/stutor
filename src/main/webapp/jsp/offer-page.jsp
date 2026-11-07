<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
  <title>${offer.title}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<div class="offer-page">
  <h1>${offer.title}</h1>
  <p class="badge">${offer.category.name}</p>

  <div class="author-info">
    <c:choose>
      <c:when test="${not empty offer.author.avatar}">
        <img src="${pageContext.request.contextPath}/uploads/${offer.author.avatar.storageFileName}" width="50" height="50" style="border-radius: 50%;">
      </c:when>
      <c:otherwise>
        <img src="${pageContext.request.contextPath}/img/default-avatar.png" width="50" height="50">
      </c:otherwise>
    </c:choose>
    <span>Репетитор: ${offer.author.fullName}</span>
  </div>

  <hr>

  <div class="offer-description">
    <h3>О занятии:</h3>
    <p>${offer.description}</p>
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
          <li>${subject.name} (Курс: ${subject.course}, Факультет: ${subject.faculty.name})</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <c:if test="${not empty offer.attachment}">
    <div class="offer-attachment">
      <h3>Материалы:</h3>
      <a href="${pageContext.request.contextPath}/download?fileId=${offer.attachment.id}">
        Скачать файл (${offer.attachment.originalFileName})
      </a>
    </div>
  </c:if>
</div>
</body>
</html>