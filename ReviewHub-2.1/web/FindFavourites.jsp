<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Favorite</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
<form action="findfavorites" method="post">
    <h1>Search for Favorite by User Id</h1>
    <p>
        <label for="userId">UserId</label> <input id="userId" name="userId"
                                                  value="${fn:escapeXml(param.userId)}">
    </p>
    <p>
        <input type="submit"> <br/> <br/> <br/> <span
            id="successMessage"><b>${messages.success}</b></span>
    </p>
</form>
<br/>
<div id="createFavortites">
    <a href="createfavorites">Create Favorites</a>
</div>
<br/>
<h1>Matching Favorites</h1>
<table border="1" class="table table-striped">
    <tr>
        <th>User Id</th>
        <th>Restaurant</th>
        <th>Restaurant Detail</th>
        <th>Delete Favorite</th>
    </tr>
    <c:forEach items="${favorites}" var="favorite">
        <tr>
            <td><c:out value="${favorite.getUser().getUserId()}"/></td>
            <td><c:out value="${favorite.getRestaurant().getName()}"/></td>
            <td><a
                    href="findrestaurants?restaurantId=<c:out value="${favorite.getRestaurant().getRestaurantId()}"/>">Detail</a>
            </td>
            <td><a
                    href="deletefavorites?favoriteId=<c:out value="${favorite.getFavoriteId()}"/>">DELETE</a></td>
        </tr>
    </c:forEach>
</table>
</div>
</body>
</html>
