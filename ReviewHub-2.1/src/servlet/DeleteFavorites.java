
package servlet;

import dal.FavoritesDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Favorites;


@WebServlet("/deletefavorites")
public class DeleteFavorites extends HttpServlet {
	protected FavoritesDao favoritesDao;

	@Override
	public void init() throws ServletException {
		favoritesDao = FavoritesDao.getInstance();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Map for storing messages.
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);
		// Provide a title and render the JSP.
		messages.put("title", "Delete Favorites");
		req.getRequestDispatcher("/DeleteFavorites.jsp").forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Map for storing messages.
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);

		String restaurantId = req.getParameter("restaurantId");
		String userId = req.getParameter("userId");
		if (restaurantId == null || restaurantId.trim().isEmpty()) {
			messages.put("success", "Invalid restaurant id");
		} else if (userId == null || userId.trim().isEmpty()) {
			messages.put("success", "Invalid userId id");
		} else {
			Favorites favorite;
			try {
				favorite = favoritesDao.getFavoriteByResIdAndUserId(Integer.parseInt(restaurantId), Integer.parseInt(userId));
				int currentFavId = favorite.getFavoriteId();
				favorite = favoritesDao.delete(favorite);
				if (favorite == null) {
					messages.put("title", "Successfully deleted favorite id: " + currentFavId +  ", restaurant id: " + restaurantId  + ", userid: " + userId);
					messages.put("disableSubmit", "true");
				} else {
					messages.put("title", "Successfully deleted favorite id: " + currentFavId +  ", restaurant id: " + restaurantId  + ", userid: " + userId);
					messages.put("disableSubmit", "false");
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}

		req.getRequestDispatcher("/DeleteFavorites.jsp").forward(req, resp);
	}
}
