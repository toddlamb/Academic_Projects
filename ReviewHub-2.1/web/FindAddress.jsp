<%--
  Created by IntelliJ IDEA.
  User: bryanfuh
  Date: 12/5/17
  Time: 1:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Address</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<form action="findaddress" method="post">
    <h1>Search for Address by Restaurant Id</h1>
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
<div id="addressescreate"><a href="addressescreate">Create Address</a></div>
<br/>
<h1>Matching Address</h1>
<table border="1" class="table table-striped">
    <tr>
        <th>AddressId</th>
        <th>Restaurant</th>
        <th>Restaurant Detail</th>
        <th>Formatted Address</th>
        <th>Latitude</th>
        <th>Longitude</th>
        <th>Street One</th>
        <th>Street Two</th>
        <th>City</th>
        <th>State</th>
        <th>Country</th>
        <th>Zip Code</th>
        <th>Formatted Phone Number</th>
        <th>International Phone Number</th>
        <th>Delete Address</th>
    </tr>
    <c:forEach items="${addresses}" var="addresses">
        <tr>
            <td><c:out value="${addresses.getAddressId()}"/></td>
            <td><c:out value="${addresses.getRestaurant().getName()}"/></td>
            <td><a
                    href="findrestaurants?restaurantId=<c:out value="${addresses.getRestaurant().getRestaurantId()}"/>">Detail</a>
            </td>
            <td><c:out value="${addresses.getFormattedAddress()}"/></td>
            <td><c:out value="${addresses.getLatitude()}"/></td>
            <td><c:out value="${addresses.getLongitude()}"/></td>
            <td><c:out value="${addresses.getStreetOne()}"/></td>
            <td><c:out value="${addresses.getStreetTwo()}"/></td>
            <td><c:out value="${addresses.getCity()}"/></td>
            <td><c:out value="${addresses.getState()}"/></td>
            <td><c:out value="${addresses.getCountry()}"/></td>
            <td><c:out value="${addresses.getZipCode()}"/></td>
            <td><c:out value="${addresses.getFormattedPhoneNumber()}"/></td>
            <td><c:out value="${addresses.getInternationalPhoneNumber()}"/></td>
            <td><a href="addressdelete?addressid=<c:out value="${addresses.getAddressId()}"/>">Delete</a></td>

        </tr>
    </c:forEach>
</table>
</body>
</html>
