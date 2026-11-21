<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Создать объявление">
    <jsp:attribute name="headExtra">
        <script src="https://code.jquery.com/jquery-3.5.1.js"
                integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
                crossorigin="anonymous"></script>
    </jsp:attribute>
    <jsp:body>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form class="create-offer-form" action="${pageContext.request.contextPath}/create-offer" method="post" enctype="multipart/form-data">
            <input type="text" name="title" placeholder="Введите заголовок объявления" required>
            <div class="offer-description">
                <input type="text" name="description" placeholder="Описание тьюторства">
            </div>

            <select name="format" required>
                <c:forEach var="format" items="${offerFormats}">
                    <option value="${format}">${format}</option>
                </c:forEach>
            </select>

            <input type="text" class="price" name="price" placeholder="Введите цену" required>
            <select name="categoryId" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>

            <div class="subjects-section">
                <p class="section-label">Дисциплины (выберите одну или несколько):</p>

                <div class="subject-filters">
                    <select id="faculty-select">
                        <option value="" disabled selected>Выберите институт...</option>
                        <c:forEach var="faculty" items="${faculties}">
                            <option value="${faculty.id}">${faculty.name}</option>
                        </c:forEach>
                    </select>

                    <select id="course-select">
                        <option value="" disabled selected>Выберите курс...</option>
                        <option value="1">1 курс</option>
                        <option value="2">2 курс</option>
                        <option value="3">3 курс</option>
                        <option value="4">4 курс</option>
                    </select>
                </div>

                <div class="subjects-scroll-box" id="subjectsContainer">
                    <span class="placeholder-text">Сначала выберите институт и курс</span>
                </div>
            </div>

            <div class="file-upload-section">
                <p class="section-label">Прикрепить файл (необязательно):</p>
                <input type="file" name="file" id="offerFile">
            </div>

            <button type="submit">Опубликовать объявление</button>
        </form>

        <script>
            const contextPath = '${pageContext.request.contextPath}';
        </script>
        <script src="${pageContext.request.contextPath}/js/subject-filter.js"></script>
    </jsp:body>
</t:layout>