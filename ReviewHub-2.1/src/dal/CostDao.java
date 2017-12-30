package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Cost;
import model.Restaurants;

public class CostDao {

  private ConnectionManager connectionManager;
  private RestaurantsDao restaurantsDao;
  private static CostDao costDao;

  private CostDao() {
    this.connectionManager = new ConnectionManager();
    this.restaurantsDao = RestaurantsDao.getInstance();
  }

  public static CostDao getInstance() {
    if (costDao == null) {
      costDao = new CostDao();
    }
    return costDao;
  }

  public Cost create(Cost cost) throws SQLException {
    String sqlTemplate = "INSERT INTO Cost(RestaurantId,PriceRange) VALUES(?,?);";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, cost.getRestaurant().getRestaurantId());
      preparedStatement.setInt(2, cost.getPriceRange());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        cost.setCostId(resultSet.getInt(1));
      }
      return cost;
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

  public Cost updatePriceRange(Cost cost, Integer newPriceRange) throws SQLException {
    String sqlTemplate = "UPDATE Cost SET PriceRange = ? WHERE CostId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, cost.getPriceRange());
      preparedStatement.setInt(2,cost.getCostId());
      preparedStatement.executeUpdate();
      cost.setPriceRange(newPriceRange);
      return cost;
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

  public Cost getCostById(Integer costId) throws SQLException {
    String sqlTemplate = "SELECT * FROM Cost WHERE CostId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Cost cost = null;
    Restaurants restaurant = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, costId);
      ResultSet resultSet = preparedStatement.executeQuery();
      restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
      if (resultSet.next()) {
       cost = new Cost(
           resultSet.getInt("CostId"),
           restaurant,
           resultSet.getInt("PriceRange")
       );
      }
      return cost;
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

  public Cost delete(Cost cost) throws SQLException {
    String sqlTemplate = "DELETE FROM Cost WHERE CostId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, cost.getCostId());
      preparedStatement.executeUpdate();
      return cost;
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
}
