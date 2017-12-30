package dal;

import model.FoodSafety;
import model.Restaurants;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodSafetyDao {
    private ConnectionManager connectionManager;
    private RestaurantsDao restaurantsDao;
    private static FoodSafetyDao foodSafetyDao;

    private FoodSafetyDao() {
        this.connectionManager = new ConnectionManager();
        this.restaurantsDao = RestaurantsDao.getInstance();
    }

    public static FoodSafetyDao getInstance() {
        if (foodSafetyDao == null) {
            foodSafetyDao = new FoodSafetyDao();
        }
        return foodSafetyDao;
    }

    public FoodSafety create(FoodSafety foodSafety) throws SQLException {
        String sqlTemplate = "INSERT INTO FoodSafety(RestaurantId,InspectionScore,InspectionResult,InspectionDate) VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, foodSafety.getRestaurant().getRestaurantId());
            preparedStatement.setInt(2, foodSafety.getInspectionScore());
            preparedStatement.setString(3, foodSafety.getInspectionResult());
            preparedStatement.setTimestamp(4, new Timestamp(foodSafety.getInspectionDate().getTime()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                foodSafety.setFoodSafetyId(resultSet.getInt(1));
            }
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public FoodSafety getByFoodSafetyId(Integer foodSafetyId) throws SQLException {
        String sqlTemplate = "SELECT * FROM FoodSafety WHERE FoodSafetyId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        FoodSafety foodSafety = null;
        Restaurants restaurant = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, foodSafetyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
            if (resultSet.next()) {
                foodSafety = new FoodSafety(
                        resultSet.getInt("FoodSafetyId"),
                        restaurant,
                        resultSet.getInt("InspectionScore"),
                        resultSet.getString("InspectionResult"),
                        resultSet.getDate("InspectionDate")
                );
            }
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }



    public FoodSafety updateInspectionScore(FoodSafety foodSafety, Integer newScore, String newResult, Date newDate) throws SQLException {
        String sqlTemplate = "UPDATE FoodSafety SET InspectionScore = ?, InspectionResult = ?, InspectionDate = ? WHERE FoodSafetyId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, newScore);
            preparedStatement.setString(2, newResult);
            preparedStatement.setTimestamp(3, new Timestamp(newDate.getTime()));
            preparedStatement.setInt(4, foodSafety.getFoodSafetyId());
            preparedStatement.executeUpdate();
            foodSafety.setInspectionScore(newScore);
            foodSafety.setInspectionResult(newResult);
            foodSafety.setInspectionDate(newDate);
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public List<FoodSafety> getFoodSafetyByRestaurantId(int restaurantId) throws SQLException {
        List<FoodSafety> foodsafetylist = new ArrayList<>();
        String selectFoodSafeties = "SELECT * FROM FoodSafety WHERE RestaurantId = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        RestaurantsDao restaurantsDao = RestaurantsDao.getInstance();
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectFoodSafeties);
            selectStmt.setInt(1, restaurantId);
            results = selectStmt.executeQuery();

            while(results.next()) {
                int foodSafetyId = results.getInt("FoodSafetyId");
                int resultRestaurantId = results.getInt("RestaurantId");
                Restaurants restaurant = restaurantsDao.getRestaurantById(resultRestaurantId);
                int inspectionScore = results.getInt("InspectionScore");
                String inspectionResult = results.getString("InspectionResult");
                Date inspectionDate = new Date(results.getTimestamp("InspectionDate").getTime());

                FoodSafety review = new FoodSafety(foodSafetyId, restaurant, inspectionScore, inspectionResult, inspectionDate);
                foodsafetylist.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(selectStmt != null) {
                selectStmt.close();
            }
            if(results != null) {
                results.close();
            }
        }
        return foodsafetylist;
    }


    public FoodSafety delete(FoodSafety foodsafety) throws SQLException {
        String deletefoodsafety = "DELETE FROM FoodSafety WHERE FoodSafetyId = ?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;

        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deletefoodsafety);
            deleteStmt.setInt(1, foodsafety.getFoodSafetyId());
            deleteStmt.executeUpdate();

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }

}
