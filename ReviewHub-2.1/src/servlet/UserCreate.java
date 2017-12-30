package servlet;

import dal.*;
import javax.servlet.http.Cookie;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/usercreate")
public class UserCreate extends HttpServlet {
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
    //Just render the JSP.
    req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);
  }


  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);


    // Create the user.
    String firstName = req.getParameter("firstname");
    String lastName = req.getParameter("lastname");
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    try {
      Users user = new Users(firstName, lastName, email, password);
      user = userDao.create(user);
      Cookie userId = new Cookie("user_id",new Integer(user.getUserId()).toString());
      userId.setMaxAge(60*60*24);
      resp.addCookie(userId);
      messages.put("success", "Successfully created " +  "user number " + user.getUserId());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
    resp.setHeader("Refresh","2;url=FindRestaurantsFilter.jsp");
    req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);

  }

}
