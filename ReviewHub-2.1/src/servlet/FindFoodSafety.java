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

@WebServlet("/findfoodsafety")
public class FindFoodSafety extends HttpServlet {
    protected FoodSafetyDao foodSafetyDao;

    @Override
    public void init() throws ServletException {
        foodSafetyDao = FoodSafetyDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<FoodSafety> foodSafety = new ArrayList<>();

        String restaurantId = req.getParameter("restaurantid");
        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid restaurant id.");
        } else {
            try {
                foodSafety = foodSafetyDao.getFoodSafetyByRestaurantId(Integer.parseInt(restaurantId));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying foodSafety for restaurant " + restaurantId);
        }
        req.setAttribute("foodSafety", foodSafety);

        req.getRequestDispatcher("/FindFoodSafety.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<FoodSafety> foodSafety = new ArrayList<>();

        String restaurantId = req.getParameter("restaurantid");
        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid restaurant id.");
        } else {
            try {
                foodSafety = foodSafetyDao.getFoodSafetyByRestaurantId(Integer.parseInt(restaurantId));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying foodSafety for restaurant " + restaurantId);
        }
        req.setAttribute("foodSafety", foodSafety);

        req.getRequestDispatcher("/FindFoodSafety.jsp").forward(req, resp);
    }
}
