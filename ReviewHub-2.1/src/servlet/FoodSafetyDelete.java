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

import dal.FoodSafetyDao;
import model.FoodSafety;


@WebServlet("/foodsafetydelete")
public class FoodSafetyDelete extends HttpServlet {
    protected FoodSafetyDao foodsafetyDao;

    @Override
    public void init() throws ServletException {
        foodsafetyDao = FoodSafetyDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        messages.put("title", "Delete Food Safety");
        req.getRequestDispatcher("/FoodSafetyDelete.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String foodsafetyid = req.getParameter("foodsafetyid");
        if (foodsafetyid == null || foodsafetyid.trim().isEmpty()) {
            messages.put("title", "Invalid food safety id");
            messages.put("disableSubmit", "true");
        } else {
            FoodSafety foodsafety = new FoodSafety(Integer.parseInt(foodsafetyid));
            try {
                foodsafety = foodsafetyDao.delete(foodsafety);
                if (foodsafety == null) {
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

        req.getRequestDispatcher("/FoodSafetyDelete.jsp").forward(req, resp);
    }
}
