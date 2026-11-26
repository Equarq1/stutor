<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Регистрация">
    <jsp:attribute name="headExtra">
        <script src="https://code.jquery.com/jquery-3.5.1.js"
                integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
                crossorigin="anonymous"></script>
    </jsp:attribute>
    <jsp:body>
        <c:if test="${not empty errorMessage}">
            <div class="error-message"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/registration" method="post">
            <input type="text" name="email" placeholder="Введите почту" required>
            <input type="password" name="password" placeholder="Введите пароль" required>
            <input type="text" name="fullName" placeholder="Введите ФИО" required>

            <select id="faculty-select" required>
                <option value="" disabled selected>Выберите факультет...</option>
                <c:forEach items="${faculties}" var="faculty">
                    <option value="${faculty.id}"><c:out value="${faculty.name}"/></option>
                </c:forEach>
            </select>

            <select id="course-select" required>
                <option value="" disabled selected>Выберите курс...</option>
                <c:forEach begin="1" end="4" var="i">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>

            <select name="groupId" id="group-select" required>
                <option value="" disabled selected>Сначала выберите факультет и курс...</option>
            </select>

            <select name="cityId" required>
                <option value="" disabled selected>Выберите город...</option>
                <c:forEach items="${cities}" var="city">
                    <option value="${city.id}"><c:out value="${city.name}"/></option>
                </c:forEach>
            </select>
            <button type="submit">Зарегистрироваться</button>
        </form>

        <script>
            const contextPath = '${pageContext.request.contextPath}';
        </script>
        <script src="${pageContext.request.contextPath}/js/registration-form.js"></script>
    </jsp:body>
</t:layout>