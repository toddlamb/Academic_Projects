
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Favorites;
import model.Restaurants;
import model.Users;

public class FavoritesDao {
  protected ConnectionManager connectionManager;
  private static FavoritesDao instance = null;

  protected FavoritesDao() {
    connectionManager = new ConnectionManager();
  }

  public static FavoritesDao getInstance() {
    if (instance == null) {
      instance = new FavoritesDao();
    }
    return instance;
  }

  public Favorites create(Favorites favorite) throws SQLException {
    String insertFavorites = "INSERT INTO Favorites(RestaurantId, UserId) "
        + "VALUES(?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;

    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertFavorites, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setInt(1, favorite.getRestaurant().getRestaurantId());
      insertStmt.setInt(2, favorite.getUser().getUserId());
      insertStmt.executeUpdate();

      resultKey = insertStmt.getGeneratedKeys();
      int favoriteId = -1;
      if (resultKey.next()) {
        favoriteId = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      favorite.setFavoriteId(favoriteId);
      return favorite;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }
  }

  public Favorites getFavoriteById(int favoriteId) throws SQLException {
    String selectFavorite = "SELECT * FROM Favorites WHERE FavoriteId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectFavorite);
      selectStmt.setInt(1, favoriteId);
      results = selectStmt.executeQuery();
      RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
      UsersDao userDao = UsersDao.getInstance();

      if (results.next()) {
        int resultFavoriteId = results.getInt("FavoriteId");
        int restaurantId = results.getInt("RestaurantId");
        Restaurants restaurant = restaurantDao.getRestaurantById(restaurantId);
        int userId = results.getInt("UserId");
        Users user = userDao.getUserById(userId);
        Favorites favorite = new Favorites(resultFavoriteId, restaurant, user);
        return favorite;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return null;
  }

  public Favorites getFavoriteByResIdAndUserId(int restaurantId, int userId) throws SQLException {
    String selectFavorite = "SELECT * FROM Favorites WHERE RestaurantId = ? AND UserId = ?";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectFavorite);
      selectStmt.setInt(1, restaurantId);
      selectStmt.setInt(2, userId);

      results = selectStmt.executeQuery();
      RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
      UsersDao userDao = UsersDao.getInstance();

      if (results.next()) {
        int favoriteId = results.getInt("FavoriteId");
        int resultRestaurantId = results.getInt("RestaurantId");
        Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
        int resultUserId = results.getInt("UserId");
        Users user = userDao.getUserById(resultUserId);
        Favorites favorite = new Favorites(favoriteId, restaurant, user);
        return favorite;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return null;
  }

  public List<Favorites> getFavoriteByRestaurantId(int restaurantId) throws SQLException {
    List<Favorites> favorites = new ArrayList<Favorites>();
    String selectFavorite = "SELECT * FROM Favorites WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
    UsersDao userDao = UsersDao.getInstance();

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectFavorite);
      selectStmt.setInt(1, restaurantId);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int favoriteId = results.getInt("FavoriteId");
        int resultRestaurantId = results.getInt("RestaurantId");
        Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
        int userId = results.getInt("UserId");
        Users user = userDao.getUserById(userId);
        Favorites favorite = new Favorites(favoriteId, restaurant, user);
        favorites.add(favorite);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return favorites;
  }

  public List<Favorites> getFavoriteByRUserId(int userId) throws SQLException {
    List<Favorites> favorites = new ArrayList<Favorites>();
    String selectFavorite = "SELECT * FROM Favorites WHERE UserId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
    UsersDao userDao = UsersDao.getInstance();

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectFavorite);
      selectStmt.setInt(1, userId);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int favoriteId = results.getInt("FavoriteId");
        int RestaurantId = results.getInt("RestaurantId");
        Restaurants restaurant = restaurantDao.getRestaurantById(RestaurantId);
        int resultUserId = results.getInt("UserId");
        Users user = userDao.getUserById(resultUserId);
        Favorites favorite = new Favorites(favoriteId, restaurant, user);
        favorites.add(favorite);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return favorites;
  }

  public Favorites delete(Favorites favorite) throws SQLException {
    String deleteFavorite = "DELETE FROM Favorites WHERE FavoriteId = ?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;

    try {
      connection = connectionManager.getConnection();
      deleteStmt = connection.prepareStatement(deleteFavorite);
      deleteStmt.setInt(1, favorite.getFavoriteId());
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
