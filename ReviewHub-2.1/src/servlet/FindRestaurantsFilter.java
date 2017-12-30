package servlet;

import dal.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/findrestaurantsfilter")
public class FindRestaurantsFilter extends HttpServlet {

  protected RestaurantsDao restaurantsDao;

  @Override
  public void init() throws ServletException {
    restaurantsDao = RestaurantsDao.getInstance();
  }

  private static void putIfPresent(Map<String,String> map, String key, String value) {
    if (value != null && !value.trim().isEmpty()) {
     map.put(key,value);
    }
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> params = new HashMap<>();
    req.setAttribute("params", params);
    String city, state, cuisine, name, isOpen, sort;
    putIfPresent(params,"city", city = req.getParameter("city"));
    putIfPresent(params,"state", state = req.getParameter("state"));
    putIfPresent(params,"cuisine", cuisine = req.getParameter("cuisine"));
    putIfPresent(params,"name", name = req.getParameter("name"));
    putIfPresent(params,"isOpen", isOpen = req.getParameter("isOpen"));
    putIfPresent(params,"sort", sort = req.getParameter("sort"));
    List<MainPageData> data = null;
    try {
      data = restaurantsDao.getRestaurantsByParams(params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
    req.setAttribute("results",data);
    req.getRequestDispatcher("/FindRestaurantsFilter.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    List<Restaurants> restaurants = new ArrayList<>();

    String city = req.getParameter("city");
    String state = req.getParameter("state");
    String cuisine = req.getParameter("cuisine");
    String sort = req.getParameter("sort");
    if ((city == null || city.trim().isEmpty()) || (state == null || state.trim().isEmpty())
        || (cuisine == null || cuisine.trim().isEmpty())) {
      messages.put("success", "Please enter a valid parameter.");
    } else {
      try {
        restaurants = restaurantsDao.getRestaurantFilter(city, state, cuisine);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying results");
    }
    req.setAttribute("restaurants", restaurants);

    req.getRequestDispatcher("/FindRestaurantsFilter.jsp").forward(req, resp);
  }
}
