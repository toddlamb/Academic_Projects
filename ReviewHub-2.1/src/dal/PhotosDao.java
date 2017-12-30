package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Photos;
import model.Restaurants;

public class PhotosDao {

  private ConnectionManager connectionManager;
  private RestaurantsDao restaurantsDao;
  private static PhotosDao photosDao;

  private PhotosDao() {
    this.connectionManager = new ConnectionManager();
    this.restaurantsDao = RestaurantsDao.getInstance();
  }

  public static PhotosDao getInstance() {
    if (photosDao == null) {
      photosDao = new PhotosDao();
    }
    return photosDao;
  }

  public Photos create(Photos photos) throws SQLException {
    String sqlTemplate = "INSERT INTO Photos(RestaurantId,Content) VALUES(?,?);";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, photos.getRestaurant().getRestaurantId());
      preparedStatement.setString(2, photos.getContent());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        photos.setPhotoId(resultSet.getInt(1));
      }
      return photos;
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


  public List<Photos> getPhotosByRestaurantId(int restaurantId) throws SQLException {
    List<Photos> photoList = new ArrayList<>();
    String selectFoodSafeties = "SELECT * FROM Photos WHERE RestaurantId = ?;";
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
        int photoId = results.getInt("PhotoId");
        int resultRestaurantId = results.getInt("RestaurantId");
        Restaurants restaurant = restaurantsDao.getRestaurantById(resultRestaurantId);
        String content = results.getString("Content");
        Photos photo = new Photos(photoId, restaurant, content);
        photoList.add(photo);
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
    return photoList;
  }

  public Photos getByPhotoId(Integer photoId) throws SQLException {
    String sqlTemplate = "SELECT * FROM Photos WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Photos photo = null;
    Restaurants restaurant = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, photoId);
      ResultSet resultSet = preparedStatement.executeQuery();
      restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
      if (resultSet.next()) {
        photo = new Photos(
            resultSet.getInt("PhotoId"),
            restaurant,
            resultSet.getString("Content")
        );
      }
      return photo;
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

  public Photos updatePhotoLink(Photos photos, String newLink) throws SQLException {
    String sqlTemplate = "UPDATE Photos SET Content = ? WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setString(1, newLink);
      preparedStatement.setInt(2, photos.getPhotoId());
      preparedStatement.executeUpdate();
      photos.setContent(newLink);
      return photos;
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

  public Photos delete(Photos photos) throws SQLException {
    String sqlTemplate = "DELETE FROM Photos WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, photos.getPhotoId());
      preparedStatement.executeUpdate();
      return photos;
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

