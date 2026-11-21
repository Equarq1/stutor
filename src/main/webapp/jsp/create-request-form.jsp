<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Откликнуться на: ${offer.title}">
    <main>
        <h2>Оставить заявку репетитору</h2>
        <form action="${pageContext.request.contextPath}/request/create" method="post" enctype="multipart/form-data">
            <input type="hidden" name="offerId" value="${offer.id}">

            <label>Выберите предмет:</label>
            <select name="subjectId" required>
                <c:forEach var="subject" items="${offer.subjects}">
                    <option value="${subject.id}">${subject.name}</option>
                </c:forEach>
            </select>

            <label>Сообщение репетитору:</label>
            <textarea name="message" rows="5" placeholder="Напишите, какие темы хотите подтянуть..." required></textarea>

            <label>Прикрепить файл (например, свою домашку):</label>
            <input type="file" name="file">

            <button type="submit">Отправить заявку</button>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>
        </form>
    </main>
</t:layout>