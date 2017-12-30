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

@WebServlet("/findaddress")
public class FindAddress extends HttpServlet {
    protected AddressesDao addressDao;

    @Override
    public void init() throws ServletException {
        addressDao = AddressesDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<Addresses> addresses = new ArrayList<>();

        String restaurantId = req.getParameter("restaurantid");
        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid restaurant id.");
        } else {
            try {
                addresses = addressDao.getAddressesByRestaurantId(Integer.parseInt(restaurantId));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying addresses for restaurant " + restaurantId);
        }
        req.setAttribute("addresses", addresses);

        req.getRequestDispatcher("/FindAddress.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<Addresses> addresses = new ArrayList<>();

        String restaurantId = req.getParameter("restaurantid");
        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid restaurant id.");
        } else {
            try {
                addresses = addressDao.getAddressesByRestaurantId(Integer.parseInt(restaurantId));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying addresses for restaurant " + restaurantId);
        }
        req.setAttribute("addresses", addresses);

        req.getRequestDispatcher("/FindAddress.jsp").forward(req, resp);
    }
}
