<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create a FoodSafety</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h1>Create FoodSafety</h1>
    <form action="foodsafetycreate" method="post">
        <p>
            <label for=restaurantid>Restaurant Id</label>
            <input id="restaurantid" name="restaurantid" value="">
        </p>
        <p>
            <label for="InspectionScore">Inspection Score</label>
            <input id="InspectionScore" name="InspectionScore" value="">
        </p>
        <p>
            <label for="InspectionResult">Inspection Result</label>
            <input id="InspectionResult" name="InspectionResult" value="">
        </p>
        <p>
            <label for="InspectionDate">Inspection Date</label>
            <input id="InspectionDate" name="InspectionDate" value="">
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