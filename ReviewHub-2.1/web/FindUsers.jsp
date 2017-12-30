<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find a User</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<form action="findusers" method="post">
    <h1>Search for a User by First Name</h1>
    <p>
        <label for="firstname">FirstName</label>
        <input id="firstname" name="firstname" value="${fn:escapeXml(param.firstname)}">
    </p>
    <p>
        <input type="submit">
        <br/><br/><br/>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</form>
<br/>
<div id="userCreate"><a href="usercreate">Create User</a></div>
<br/>
<h1>Matching Users</h1>
<table border="1" class="table table-striped">
    <tr>
        <th>User Id</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Favorites</th>
        <th>Delete User</th>
        <th>Update User</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><c:out value="${user.getUserId()}"/></td>
            <td><c:out value="${user.getFirstName()}"/></td>
            <td><c:out value="${user.getLastName()}"/></td>
            <td><c:out value="${user.getEmail()}"/></td>
            <td><a href="findfavorites?userid=<c:out value="${user.getUserId()}"/>">Favorites</a></td>
            <td><a href="userdelete?userid=<c:out value="${user.getUserId()}"/>">Delete</a></td>
            <td><a href="userupdate?userid=<c:out value="${user.getUserId()}"/>">Update</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>