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

import dal.PhotosDao;
import model.Photos;


@WebServlet("/photodelete")
public class DeletePhoto extends HttpServlet {
    protected PhotosDao photoDao;

    @Override
    public void init() throws ServletException {
        photoDao = PhotosDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        messages.put("title", "Delete Photo");
        req.getRequestDispatcher("/PhotoDelete.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String photoId = req.getParameter("photoid");
        if (photoId == null || photoId.trim().isEmpty()) {
            messages.put("title", "Invalid PhotoId");
            messages.put("disableSubmit", "true");
        } else {
            Photos photo = new Photos(Integer.parseInt(photoId));
            try {
                photo = photoDao.delete(photo);
                if (photo == null) {
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

        req.getRequestDispatcher("/PhotoDelete.jsp").forward(req, resp);
    }
}
