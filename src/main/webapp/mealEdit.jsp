<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Edit Meal</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Edit Meal</h2>
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime</dt>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <fmt:formatDate var="formatedDateTime" pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
            <dd><input type="datetime-local" name="dateTime" value="${formatedDateTime}"></dd>
        </dl>
        <dl>
            <dt>Description</dt>
            <dd><input type="text" name="description" value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Calories</dt>
            <dd><input type="number" name="calories" value="${meal.calories}"></dd>
        </dl>
        <div class="flex btn-wrapper">
            <button type="submit">Сохранить</button>
            <button type="button" onclick="window.history.back()">Отменить</button>
        </div>
    </form>
</body>
</html>