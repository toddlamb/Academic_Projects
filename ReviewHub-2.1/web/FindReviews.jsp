<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Reviews</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<form action="findreviews" method="post">
    <h1>Search for Review by Restaurant Id</h1>
    <p>
        <label for="restaurantid">RestaurantId</label>
        <input id="restaurantid" name="restaurantid" value="${fn:escapeXml(param.restaurantid)}">
    </p>
    <p>
        <input type="submit">
        <br/><br/><br/>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</form>
<br/>
<div id="reviewCreate"><a href="reviewcreate">Create Review</a></div>
<br/>
<h1>Matching Reviews</h1>
<table border="1" class="table table-striped">
    <tr>
        <th>Review Id</th>
        <th>Restaurant</th>
        <th>Restaurant Detail</th>
        <th>Value</th>
        <th>Content</th>
        <th>Delete Review</th>
    </tr>
    <c:forEach items="${reviews}" var="reviews">
        <tr>
            <td><c:out value="${reviews.getReviewId()}"/></td>
            <td><c:out value="${reviews.getRestaurant().getName()}"/></td>
            <td><a
                    href="findrestaurants?restaurantId=<c:out value="${reviews.getRestaurant().getRestaurantId()}"/>">Detail</a>
            </td>
            <td><c:out value="${reviews.getValue()}"/></td>
            <td><c:out value="${reviews.getContent()}"/></td>
            <td><a href="reviewdelete?reviewid=<c:out value="${reviews.getReviewId()}"/>">Delete</a></td>

        </tr>
    </c:forEach>
</table>
</body>
</html>
