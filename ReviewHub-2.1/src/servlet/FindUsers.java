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

@WebServlet("/findusers")
public class FindUsers extends HttpServlet {
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

    List<Users> users = new ArrayList<Users>();

    String firstName = req.getParameter("firstname");
    if (firstName == null || firstName.trim().isEmpty()) {
      messages.put("success", "Please enter a valid name.");
    } else {
      try {
        users = userDao.getUsersFromFirstName(firstName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying results for " + firstName);
      messages.put("previousFirstName", firstName);
    }
    req.setAttribute("users", users);

    req.getRequestDispatcher("/FindUsers.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);
    List<Users> users = new ArrayList<Users>();
    String firstName = req.getParameter("firstname");
    if (firstName == null || firstName.trim().isEmpty()) {
      messages.put("success", "Please enter a valid name.");
    } else {
      try {
        users = userDao.getUsersFromFirstName(firstName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying results for " + firstName);
    }
    req.setAttribute("users", users);

    req.getRequestDispatcher("/FindUsers.jsp").forward(req, resp);
  }
}
