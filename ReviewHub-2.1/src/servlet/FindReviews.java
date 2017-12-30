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

@WebServlet("/findreviews")
public class FindReviews extends HttpServlet {
  protected ReviewsDao reviewsDao;

  @Override
  public void init() throws ServletException {
    reviewsDao = ReviewsDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    List<Reviews> reviews = new ArrayList<>();

    String restaurantId = req.getParameter("restaurantid");
    if (restaurantId == null || restaurantId.trim().isEmpty()) {
      messages.put("success", "Please enter a valid restaurant id.");
    } else {
      try {
        reviews = reviewsDao.getReviewsByRestaurantId(Integer.parseInt(restaurantId));
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying reviews for restaurant " + restaurantId);
    }
    req.setAttribute("reviews", reviews);

    req.getRequestDispatcher("/FindReviews.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    List<Reviews> reviews = new ArrayList<>();

    String restaurantId = req.getParameter("restaurantid");
    if (restaurantId == null || restaurantId.trim().isEmpty()) {
      messages.put("success", "Please enter a valid restaurant id.");
    } else {
      try {
         reviews = reviewsDao.getReviewsByRestaurantId(Integer.parseInt(restaurantId));
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying reviews for restaurant " + restaurantId);
    }
    req.setAttribute("reviews", reviews);

    req.getRequestDispatcher("/FindReviews.jsp").forward(req, resp);
  }
}
