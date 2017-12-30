package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dal.*;
import model.*;


@WebServlet("/findfavorites")
public class FindFavorites extends HttpServlet {

  protected FavoritesDao favoritesDao;

  @Override
  public void init() throws ServletException {
	  favoritesDao = FavoritesDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  Map<String, String> messages = new HashMap<String, String>();
      req.setAttribute("messages", messages);

      List<Favorites> favorites = new ArrayList<Favorites>();
      String userId = req.getParameter("userId");
      if (userId == null || userId.trim().isEmpty()) {
          messages.put("success", "Please enter a valid user id.");
      } else {
      	try {
      		favorites = favoritesDao.getFavoriteByRUserId(Integer.parseInt(userId));
          } catch (SQLException e) {
  			e.printStackTrace();
  			throw new IOException(e);
          }
      	messages.put("success", "Displaying results for " + userId);
      	messages.put("previousUserId", userId);
      }
      req.setAttribute("favorites", favorites);
      req.getRequestDispatcher("/FindFavourites.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  Map<String, String> messages = new HashMap<String, String>();
      req.setAttribute("messages", messages);

      List<Favorites> favorites = new ArrayList<Favorites>();
      String userId = req.getParameter("userId");
      if (userId == null || userId.trim().isEmpty()) {
          messages.put("success", "Please enter a valid user id.");
      } else {
      	try {
      		favorites = favoritesDao.getFavoriteByRUserId(Integer.parseInt(userId));
          } catch (SQLException e) {
  			e.printStackTrace();
  			throw new IOException(e);
          }
      	messages.put("success", "Displaying results for " + userId);
      	messages.put("previousUserId", userId);
      }
      req.setAttribute("favorites", favorites);
      req.getRequestDispatcher("/FindFavourites.jsp").forward(req, resp);

  }


  @Override
  public void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
  }


  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

  }

}
