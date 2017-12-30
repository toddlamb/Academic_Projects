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

import dal.UsersDao;
import model.Users;

@WebServlet("/userdelete")
public class UserDelete extends HttpServlet {
  protected UsersDao userDao;

  @Override
  public void init() throws ServletException {
    userDao = UsersDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);
    // Provide a title and render the JSP.
    messages.put("title", "Delete User");
    req.getRequestDispatcher("/UserDelete.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    // Retrieve and validate.
    String userId = req.getParameter("userid");
    if (userId == null || userId.trim().isEmpty()) {
      messages.put("title", "Invalid UserId");
      messages.put("disableSubmit", "true");
    } else {
      Users user = new Users(Integer.parseInt(userId));
      try {
        user = userDao.delete(user);
        // Update the message.
        if (user == null) {
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

    req.getRequestDispatcher("/UserDelete.jsp").forward(req, resp);
  }
}
