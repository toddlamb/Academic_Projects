package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.UsersDao;
import model.Users;


@WebServlet("/login")
public class Login extends HttpServlet {

  protected UsersDao userDao;

  @Override
  public void init() throws ServletException {
    userDao = UsersDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);
    Users user = null;
    String firstName;
    String lastName;
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (email == null || email.trim().isEmpty()) {
      messages.put("success", "Please enter an email.");
    } else {
      try {
        user = userDao.getUserByEmailAndPassword(email, password);
        if (user == null) {
          messages.put("success", "User not found. Please check the email or password.");
          req.getRequestDispatcher("/Login.jsp").forward(req, resp);
          return;
        }
        firstName = user.getFirstName();
        lastName = user.getLastName();
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Hi, " + firstName + " " + lastName + ", welcome to ReviewHub!");
    }
    req.setAttribute("user", user);
    if (messages.get("success").contains("ReviewHub")) {
      resp.setHeader("Refresh", "2;url=FindRestaurantsFilter.jsp");
      Cookie userId = new Cookie("user_id", new Integer(user.getUserId()).toString());
      userId.setMaxAge(60 * 60 * 24);
      resp.addCookie(userId);
    }
    req.getRequestDispatcher("/Login.jsp").forward(req, resp);
  }

}
