<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Редактирование объявления">
    <jsp:attribute name="headExtra">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </jsp:attribute>
    <jsp:body>
        <main>
            <h1>Редактирование объявления</h1>
            <c:if test="${not empty errorMessage}">
                <div class="error-message"><c:out value="${errorMessage}"/></div>
            </c:if>

            <form action="${pageContext.request.contextPath}/offer/update" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${offer.id}">

                <label>Название:</label>
                <input type="text" name="title" value="${fn:escapeXml(offer.title)}" required>

                <label>Категория:</label>
                <select name="categoryId" required>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}" ${category.id == offer.category.id ? 'selected' : ''}>
                            <c:out value="${category.name}"/>
                        </option>
                    </c:forEach>
                </select>

                <label>Описание:</label>
                <textarea name="description" rows="5" required>${fn:escapeXml(offer.description)}</textarea>

                <label>Стоимость (руб):</label>
                <input type="number" step="0.01" name="price" value="${offer.price}" required>

                <label>Формат:</label>
                <select name="format" required>
                    <c:forEach var="fmt" items="${offerFormats}">
                        <option value="${fmt}" ${fmt == offer.format ? 'selected' : ''}>${fmt}</option>
                    </c:forEach>
                </select>

                <hr>
                <h3>Дисциплины</h3>
                <label>Выберите факультет для поиска предметов:</label>
                <select id="faculty-select">
                    <option value="" disabled selected>Выберите факультет...</option>
                    <c:forEach var="faculty" items="${faculties}">
                        <option value="${faculty.id}"><c:out value="${faculty.name}"/></option>
                    </c:forEach>
                </select>

                <label>Курс:</label>
                <select id="course-select">
                    <option value="" disabled selected>Курс...</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                </select>

                <div id="subjectsContainer">
                    <c:forEach var="subject" items="${offer.subjects}">
                        <label class="subject-chip">
                            <input type="checkbox" name="subjectIds" value="${subject.id}" checked>
                            <span><c:out value="${subject.name}"/></span>
                        </label>
                    </c:forEach>
                </div>
                <hr>

                <label>Прикрепить новый файл (старый: <c:out value="${not empty offer.attachment ? offer.attachment.originalFileName : 'нет'}"/>)</label>
                <input type="file" name="file">

                <button type="submit">Сохранить изменения</button>
            </form>
        </main>

        <script>
            const contextPath = '${pageContext.request.contextPath}';
        </script>
        <script src="${pageContext.request.contextPath}/js/subject-filter.js"></script>
    </jsp:body>
</t:layout>