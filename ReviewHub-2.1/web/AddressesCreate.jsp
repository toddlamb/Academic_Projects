<%--
  Created by IntelliJ IDEA.
  User: bryanfuh
  Date: 12/5/17
  Time: 1:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create a Address</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h1>Create Address</h1>
    <form action="addressescreate" method="post">
        <p>
            <label for=restaurantid>Restaurant Id</label>
            <input id="restaurantid" name="restaurantid" value="">
        </p>
        <p>
            <label for="FormattedAddress">Formatted Address</label>
            <input id="FormattedAddress" name="FormattedAddress" value="">
        </p>
        <p>
            <label for="Latitude">Latitude</label>
            <input id="Latitude" name="Latitude" value="">
        </p>
        <p>
            <label for="Longitude">Longitude</label>
            <input id="Longitude" name="Longitude" value="">
        </p>
        <p>
            <label for="StreetOne">Street One</label>
            <input id="StreetOne" name="StreetOne" value="">
        </p>
        <p>
            <label for="StreetTwo">Street Two</label>
            <input id="StreetTwo" name="StreetTwo" value="">
        </p>
        <p>
            <label for="City">City</label>
            <input id="City" name="City" value="">
        </p>
        <p>
            <label for="State">State</label>
            <input id="State" name="State" value="">
        </p>
        <p>
            <label for="Country">Country</label>
            <input id="Country" name="Country" value="">
        </p>
        <p>
            <label for="ZipCode">Zip Code</label>
            <input id="ZipCode" name="ZipCode" value="">
        </p>
        <p>
            <label for="Formatted_PhoneNumber">Formatted Phone Number</label>
            <input id="Formatted_PhoneNumber" name="Formatted_PhoneNumber" value="">
        </p>
        <p>
            <label for="International_PhoneNumber">International Phone Number</label>
            <input id="International_PhoneNumber" name="International_PhoneNumber" value="">
        </p>


        <p>
            <input type="submit">
        </p>
    </form>
    <br/><br/>
    <p>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</div>
</body>
</html>