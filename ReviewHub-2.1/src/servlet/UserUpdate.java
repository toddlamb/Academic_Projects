package servlet;

import dal.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userupdate")
public class UserUpdate extends HttpServlet {
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

    // Retrieve user and validate.
    String userId = req.getParameter("userid");
    if (userId == null || userId.trim().isEmpty()) {
      messages.put("success", "Please enter a valid UserId.");
    } else {
      try {
        Users user = userDao.getUserById(Integer.parseInt(userId));
        if(user == null) {
          messages.put("success", "UserName does not exist.");
        }
        req.setAttribute("blogUser", user);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/UserUpdate.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    // Retrieve user and validate.
    String userId = req.getParameter("userid");
    if (userId == null || userId.trim().isEmpty()) {
      messages.put("success", "Please enter a valid UserId.");
    } else {
      try {
        Users user = userDao.getUserById(Integer.parseInt(userId));
        if(user == null) {
          messages.put("success", "User does not exist. No update to perform.");
        } else {
          String newLastName = req.getParameter("lastname");
          if (newLastName == null || newLastName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid LastName.");
          } else {
            user = userDao.updateLastName(user, newLastName);
            messages.put("success", "Successfully updated");
          }
        }
        req.setAttribute("user", user);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/UserUpdate.jsp").forward(req, resp);
  }
}
