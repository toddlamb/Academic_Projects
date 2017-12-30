package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.ReviewsDao;
import model.Reviews;


@WebServlet("/reviewdelete")
public class ReviewDelete extends HttpServlet {
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
    messages.put("title", "Delete Review");
    req.getRequestDispatcher("/ReviewDelete.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    String reviewId = req.getParameter("reviewid");
    if (reviewId == null || reviewId.trim().isEmpty()) {
      messages.put("title", "Invalid ReviewId");
      messages.put("disableSubmit", "true");
    } else {
      Reviews review = new Reviews(Integer.parseInt(reviewId));
      try {
        review = reviewsDao.delete(review);
        if (review == null) {
          messages.put("title", "Successfully deleted");
          messages.put("disableSubmit", "true");
        } else {
          messages.put("title", "Failed to delete ");
          messages.put("disableSubmit", "false");
        }
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/ReviewDelete.jsp").forward(req, resp);
  }
}
