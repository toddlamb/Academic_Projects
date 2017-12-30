package dal;


import model.Cuisine;
import model.Restaurants;

import java.sql.*;

public class CuisineDao {

    private ConnectionManager connectionManager;
    private RestaurantsDao restaurantsDao;
    private static CuisineDao cuisineDao;

    private CuisineDao () {
        this.connectionManager = new ConnectionManager();
        this.restaurantsDao = RestaurantsDao.getInstance();
    }

    public static CuisineDao getInstance() {
        if (cuisineDao == null) {
            cuisineDao = new CuisineDao();
        }
        return cuisineDao;
    }

    public Cuisine create(Cuisine cuisine) throws SQLException {
        String sqlTemplate = "INSERT INTO Cuisine(RestaurantId,CuisineType) VALUE(?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, cuisine.getRestaurant().getRestaurantId());
            preparedStatement.setString(2, cuisine.getCuisineType());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                cuisine.setCuisineId(resultSet.getInt(1));
            }
            return cuisine;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }


    public Cuisine getByCuisineId(Integer cuisineId) throws SQLException {
        String sqlTemplate = "SELECT * FROM Cuisine WHERE CuisideId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Cuisine cuisine = null;
        Restaurants restaurant = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, cuisineId);
            ResultSet resultSet = preparedStatement.executeQuery();
            restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
            if (resultSet.next()) {
                cuisine = new Cuisine(
                        resultSet.getInt("CuisineId"),
                        restaurant,
                        resultSet.getString("CuisineType"));
            }
            return cuisine;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }



    public Cuisine delete(Cuisine cuisine) throws SQLException {
        String sqlTemplate = "DELETE FROM Cuisine WHERE CuisideId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, cuisine.getCuisineId());
            preparedStatement.executeUpdate();
            return cuisine;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}
