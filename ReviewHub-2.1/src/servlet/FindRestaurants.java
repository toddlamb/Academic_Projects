package servlet;

import dal.RestaurantsDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Restaurants;

@WebServlet("/findrestaurants")
public class FindRestaurants extends HttpServlet {

  protected RestaurantsDao restaurantsDao;

  public enum searchInput {ID, NAME, EMPTY}

  ;

  @Override
  public void init() throws ServletException {
    restaurantsDao = RestaurantsDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String search_status = null;
    searchInput searchInput = checkParams(req);
    switch (searchInput) {
      case EMPTY:
        search_status = "Please enter a valid search parameter";
        break;
      case ID:
        Integer restaurantId = Integer.parseInt(req.getParameter("restaurantId").trim());
        search_status = "Displaying results for " + req.getParameter("restaurantId").trim();
        Restaurants restaurant = null;
        try {
          restaurant = restaurantsDao.getRestaurantById(restaurantId);
          req.setAttribute("restaurant", restaurant);
        } catch (SQLException e) {
          e.printStackTrace();
          throw new IOException(e);
        }
        break;
      case NAME:
        search_status = "Displaying results for " + req.getParameter("restaurantName").trim();
        String restaurantName = req.getParameter("restaurantName").trim();
        List<Restaurants> restaurants = new ArrayList<>();
        try {
          restaurants = restaurantsDao.getRestaurantByName(restaurantName);
          req.setAttribute("restaurants", restaurants);
        } catch (SQLException e) {
          e.printStackTrace();
          throw new IOException(e);
        }
        break;
    }
    req.setAttribute("message", search_status);
    req.getRequestDispatcher("/FindRestaurants.jsp").forward(req, resp);
  }


  private searchInput checkParams(HttpServletRequest request) {
    String restaurantId = request.getParameter("restaurantId");
    String restaurantName = request.getParameter("restaurantName");
    boolean idValid = restaurantId != null && !restaurantId.trim().isEmpty();
    boolean nameValid = restaurantName != null && !restaurantName.trim().isEmpty();
    if (!idValid && !nameValid) {
      return searchInput.EMPTY;
    } else if (idValid) {
      return searchInput.ID;
    } else {
      return searchInput.NAME;
    }
  }

}
