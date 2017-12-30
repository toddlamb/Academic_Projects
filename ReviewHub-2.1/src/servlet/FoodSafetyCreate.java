package servlet;

import dal.*;
import model.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/foodsafetycreate")
public class FoodSafetyCreate extends HttpServlet {
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
        req.getRequestDispatcher("/FoodSafetyCreate.jsp").forward(req, resp);
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String restaurantId = req.getParameter("restaurantid");
        String inspectionScore = req.getParameter("InspectionScore");
        String inspectionResult = req.getParameter("InspectionResult");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inspectionDate = req.getParameter("InspectionDate");
        java.util.Date dob = new java.util.Date();
        try {
            dob = dateFormat.parse(inspectionDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IOException();
        }

        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Invalid restaurant id");
        } else
            try {
                Restaurants res = RestaurantsDao.getInstance().getRestaurantById(Integer.parseInt(restaurantId));
                FoodSafety foodSafety = new FoodSafety(res, Integer.parseInt(inspectionScore), inspectionResult, dob);
                foodSafety = foodSafetyDao.create(foodSafety);
                messages.put("success", "Successfully created Food Safety: " + foodSafety.getFoodSafetyId());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        req.getRequestDispatcher("/FoodSafetyCreate.jsp").forward(req, resp);
    }


}
