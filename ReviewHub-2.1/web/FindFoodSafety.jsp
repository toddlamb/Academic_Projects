<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Food Safety For Restaurant</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<form action="findfoodsafety" method="post">
    <h1>Search for Food Safety by Restaurant Id</h1>
    <p>
        <label for="restaurantid">Restaurant Id</label>
        <input id="restaurantid" name="restaurantid" value="${fn:escapeXml(param.restaurantid)}">
    </p>
    <p>
        <input type="submit">
        <br/><br/><br/>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</form>
<br/>
<div id="foodsafetyCreate"><a href="foodsafetycreate">Create Food Safety</a></div>
<br/>
<h1>Matching Food Safety</h1>
<table border="1" class="table table-striped">
    <tr>
        <th>Food Safety Id</th>
        <th>Restaurant</th>
        <th>Restaurant Detail</th>
        <th>Inspection Score</th>
        <th>Inspection Result</th>
        <th>Inspection Date</th>
        <th>Delete Food Safety</th>
    </tr>
    <c:forEach items="${foodSafety}" var="foodSafety">
        <tr>
            <td><c:out value="${foodSafety.getFoodSafetyId()}"/></td>
            <td><c:out value="${foodSafety.getRestaurant().getName()}"/></td>
            <td><a
                    href="findrestaurants?restaurantId=<c:out value="${foodSafety.getRestaurant().getRestaurantId()}"/>">Detail</a>
            </td>
            <td><c:out value="${foodSafety.getInspectionScore()}"/></td>
            <td><c:out value="${foodSafety.getInspectionResult()}"/></td>
            <td><c:out value="${foodSafety.getInspectionDate()}"/></td>
            <td><a href="foodsafetydelete?foodsafetyid=<c:out value="${foodSafety.getFoodSafetyId()}"/>">Delete</a></td>

        </tr>
    </c:forEach>
</table>
</body>
</html>
