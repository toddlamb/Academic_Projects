<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Application Menu</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="Container">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">ReviewHub</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="nav-item dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        Users
                        <span class="caret"/>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="FindUsers.jsp">Search By User</a>
                        </li>
                        <li>
                            <a href="UserCreate.jsp">Create a User</a>
                        </li>
                        <li>
                            <a href="UserDelete.jsp">Delete a User</a>
                        </li>
                        <li>
                            <a href="UserUpdate.jsp">Update a User</a>
                        </li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        Favorites
                        <span class="caret"/>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="FindFavourites.jsp">Find Favorites</a>
                        </li>
                        <li>
                            <a href="FavoritesCreate.jsp">Create Favorites</a>
                        </li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="FindRestaurants.jsp">Find Restaurant</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="FindReviews.jsp">Find Reviews</a></li>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="FindAddress.jsp">Find Address</a>
                </li>
                <li class="nav-itme">
                    <a class="nav-link" href="FindFoodSafety.jsp">Find Food Safety</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="FindPhotos.jsp">Find Photos</a>
                </li>
            </ul>
        </div>
    </nav>
    <form action="findrestaurantsfilter" method="get">
        <div class="form-group form-group-sm">
            <h1>Filter By</h1>
            <p>
                <label for="name">Name</label>
                <input id="name" name="name" class="form-control input-sm"
                       value="${fn:escapeXml(param.name)}">

                <label for="city">City</label>
                <input id="city" class="form-control input-sm" name="city"
                       value="${fn:escapeXml(param.city)}">

                <label for="state">State</label>
                <input id="state" class="form-control input-sm" name="state"
                       value="${fn:escapeXml(param.state)}">

                <label for="cuisine">Cuisine Type</label>
                <input id="cuisine" class="form-control input-sm" name="cuisine"
                       value="${fn:escapeXml(param.cuisine)}">

                <label for="cost">Cost</label>
                <input id="cost" class="form-control input-sm" name="cost"
                       value="${fn:escapeXml(param.cost)}">
                <br/>
                <label class="form-check-label">
                    <input type="checkbox" id="isOpen" class="form-check-input"
                           value="${fn:escapeXml(param.isOpen)}">Currently Open
                </label>
            </p>
            <h1>Sort By</h1>
            <select name="sort" class="form-control input-sm" style="align-content: center">
                <option value="name">Name</option>
                <option value="cost">Cost</option>
            </select>
            <br/>
            <p>
                <input type="submit" class="btn btn-primary" value="Search">
                <br/><br/>
            </p>
            <h1 align="center">Matching Restaurants</h1>
        </div>
    </form>
    <table border="1" class="table table-striped">
        <tr>
            <th>Restaurant Name</th>
            <th>Has Delivery</th>
            <th>Takes Reservations</th>
            <th>Price Range</th>
            <th>Address</th>
            <th>Phone Number</th>
            <th>Cuisine Type</th>
            <th>Food Safety</th>
            <th>Reviews</th>
            <th>Pictures</th>
            <th>Yelp</th>
            <th>Google Places</th>
            <th align="center">Favorite</th>
        </tr>
        <c:forEach items="${results}" var="results">
            <tr>
                <td><c:out value="${results.getRestaurantName()}"/></td>
                <c:choose>
                    <c:when test="${results.isHasDelivery()}">
                        <td>Yes</td>
                    </c:when>
                    <c:otherwise>
                        <td>No</td>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${results.isTakesReservations()}">
                        <td>Yes</td>
                    </c:when>
                    <c:otherwise>
                        <td>No</td>
                    </c:otherwise>
                </c:choose>
                <td><c:out value="${results.getPriceRange()}"/></td>
                <td><c:out value="${results.getAddress()}"/></td>
                <td><c:out value="${results.getPhoneNumber()}"/></td>
                <td><c:out value="${results.getCuisineType()}"/></td>
                <td><a
                        href="findfoodsafety?restaurantid=<c:out value="${results.getRestaurantId()}"/>">${results.getFoodSafety()}</a>
                </td>
                <td><a
                        href="findreviews?restaurantid=<c:out value="${results.getRestaurantId()}"/>">${results.getReviews()}</a>
                </td>
                <td><a href="findphotos?restaurantid=${results.getRestaurantId()}">Photos</a></td>
                <td><a href=${results.getYelpUrl()}>Yelp</a></td>
                <td><a href=${results.getGoogleUrl()}>Google</a></td>
                <td>
                    <form action=createfavorites?restaurantId=${results.getRestaurantId()}&userId=<%
                        int userId = 0;
                        for (Cookie cookie : request.getCookies()) {
                            if (cookie.getName().equals("user_id")) {
                                userId = Integer.parseInt(cookie.getValue());
                            }
                        }
                        out.print(userId);
                    %> method="post">
                        <input type="submit" class=".btn-success" value="Add As Favorite">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>