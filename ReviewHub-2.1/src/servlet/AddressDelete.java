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

import dal.AddressesDao;
import model.Addresses;
import model.Reviews;


@WebServlet("/addressdelete")
public class AddressDelete extends HttpServlet {
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
        messages.put("title", "Delete Review");
        req.getRequestDispatcher("/AddressDelete.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String addressId = req.getParameter("addressid");
        if (addressId == null || addressId.trim().isEmpty()) {
            messages.put("title", "Invalid AddressId");
            messages.put("disableSubmit", "true");
        } else {
            Addresses address = new Addresses(Integer.parseInt(addressId));
            try {
                address = addressDao.delete(address);
                if (address == null) {
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

        req.getRequestDispatcher("/AddressDelete.jsp").forward(req, resp);
    }
}
