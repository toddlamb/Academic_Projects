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

@WebServlet("/photoscreate")
public class PhotosCreate extends HttpServlet {
    protected PhotosDao photosDao;

    @Override
    public void init() throws ServletException {
        photosDao = PhotosDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/PhotosCreate.jsp").forward(req, resp);
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String restaurantId = req.getParameter("restaurantid");
        String content = req.getParameter("content");

        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            messages.put("success", "Invalid restaurant id");
        } else
            try {
                Restaurants res = RestaurantsDao.getInstance().getRestaurantById(Integer.parseInt(restaurantId));
                Photos photos = new Photos(res, content);
                photos = photosDao.create(photos);
                messages.put("success", "Successfully created Photo: " + photos.getPhotoId());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        req.getRequestDispatcher("/PhotosCreate.jsp").forward(req, resp);
    }


}
