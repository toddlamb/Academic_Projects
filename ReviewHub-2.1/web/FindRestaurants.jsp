<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find a Restaurant</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form action="findrestaurants" method="get">
        <h1>Search for a Restaurant</h1>
        <p>
            <label for="restaurantId">Restaurant ID</label> <input id="restaurantId"
                                                                   name="restaurantId"
                                                                   value="${fn:escapeXml(param.restaurantId)}">
            <br><br>
            <label for="restaurantName">Restaurant Name</label> <input id="restaurantName"
                                                                       name="restaurantName"
                                                                       value="${fn:escapeXml(param.restaurantName)}">
        </p>
        <p>
            <input type="submit"> <br/> <br/> <br/> <span
                id="successMessage"><b>${message.toString()}</b></span>
        </p>
    </form>
    <br/>
    <br/>
    <h1>Matching Restaurants</h1>
    <table border="1" class="table table-striped">
        <tr>

            <th>Restaurant Id</th>
            <th>Restaurant Name</th>
            <th>Is Open</th>
            <th>Has Delivery</th>
            <th>Takes Reservations</th>
            <th>Reviews</th>
            <th>FoodSafety</th>
        </tr>
        <c:if test="${restaurant != null}">
            <tr>
                <td>${restaurant.getRestaurantId()}</td>
                <td>${restaurant.getName()}</td>
                <td>${restaurant.getisOpen()}</td>
                <td>${restaurant.isHasDelivery()}</td>
                <td>${restaurant.isTakesReservations()}</td>
                <td><a
                        href="findreviews?restaurantId=<c:out value="${restaurant.getRestaurantId()}"/>">Reviews</a>
                </td>
                <td><a
                        href="findfoodsafety?restaurantId=<c:out value="${restaurant.getRestaurantId()}"/>">FoodSafety</a>
                </td>
            </tr>
        </c:if>
        <c:if test="${restaurants != null}">
            <c:forEach items="${restaurants}" var="restaurant">
                <tr>
                    <td><c:out value="${restaurant.getRestaurantId()}"/></td>
                    <td><c:out value="${restaurant.getName()}"/></td>
                    <td><c:out value="${restaurant.getisOpen()}"/></td>
                    <td><c:out value="${restaurant.isHasDelivery()}"/></td>
                    <td><c:out value="${restaurant.isTakesReservations()}"/></td>
                    <td><a
                            href="findreviews?restaurantId=<c:out value="${restaurant.getRestaurantId()}"/>">Reviews</a>
                    </td>
                    <td><a
                            href="findfoodsafety?restaurantId=<c:out value="${restaurant.getRestaurantId()}"/>">FoodSafety</a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
</div>
</body>
</html>
