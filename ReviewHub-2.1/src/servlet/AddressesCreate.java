package servlet;

import dal.*;
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

@WebServlet("/addressescreate")
public class AddressesCreate extends HttpServlet {
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
        req.getRequestDispatcher("/AddressesCreate.jsp").forward(req, resp);
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String restaurantId = req.getParameter("restaurantid");
        String formattedAddress = req.getParameter("FormattedAddress");
        String lat = req.getParameter("Latitude");
        String lon = req.getParameter("Longitude");
        String s1 = req.getParameter("StreetOne");
        String s2 = req.getParameter("StreetTwo");
        String city = req.getParameter("City");
        String state = req.getParameter("State");
        String country = req.getParameter("Country");
        String zip = req.getParameter("ZipCode");
        String formatPhone = req.getParameter("Formatted_PhoneNumber");
        String intlPhone = req.getParameter("International_PhoneNumber");

        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Invalid restaurant id");
        } else
            try {
                Restaurants res = RestaurantsDao.getInstance().getRestaurantById(Integer.parseInt(restaurantId));
                Addresses addresses = new Addresses(res, formattedAddress, Double.parseDouble(lat), Double.parseDouble(lon),
                        s1, s2, city, state, country, zip, formatPhone, intlPhone);
                addresses = addressDao.create(addresses);
                messages.put("success", "Successfully created Address: " + addresses.getAddressId());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        req.getRequestDispatcher("/AddressesCreate.jsp").forward(req, resp);
    }


}
