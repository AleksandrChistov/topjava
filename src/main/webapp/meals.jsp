<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <table>
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2"></th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
        <c:forEach var="meal" items="${meals}">
            <tr style="color: ${meal.excess ? 'red' : 'green'}">
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/></td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>