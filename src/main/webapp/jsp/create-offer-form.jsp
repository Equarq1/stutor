<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script
            src="https://code.jquery.com/jquery-3.5.1.js"
            integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
            crossorigin="anonymous">
    </script>
    <title>Document</title>
</head>
<body>
<form class="create-offer-form" action="${pageContext.request.contextPath}/create-offer" method="post" enctype="multipart/form-data">
    <input type="text" name="title" placeholder="Введите заголовок объявления">
    <div class="offer-description">
        <input type="text" name="description" placeholder="Описание тьюторства">
    </div>

    <select name="format">
        <c:forEach var="format" items="${offerFormats}">
            <option value="${format}">${format}</option>
        </c:forEach>
    </select>

    <input type="text" class="price" name="price" placeholder="Введите цену">
    <select name="categoryId">
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
</body>
<script>
    const contextPath = '${pageContext.request.contextPath}'
</script>
<script src="${pageContext.request.contextPath}/js/subject-filter.js"></script>
</html>
