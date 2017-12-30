package dal;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class UsersDao {
  protected ConnectionManager connectionManager;
  private static UsersDao instance = null;

  protected UsersDao() {
    connectionManager = new ConnectionManager();
  }

  public static UsersDao getInstance() {
    if (instance == null) {
      instance = new UsersDao();
    }
    return instance;
  }

  public Users create(Users user) throws SQLException {
    String insertUser = "INSERT INTO Users(FirstName,LastName,Email,Password) "
        + "VALUES(?,?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;

    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setString(1, user.getFirstName());
      insertStmt.setString(2, user.getLastName());
      insertStmt.setString(3, user.getEmail());
      insertStmt.setString(4, user.getPassword());
      insertStmt.executeUpdate();

      resultKey = insertStmt.getGeneratedKeys();
      int userId = -1;
      if (resultKey.next()) {
        userId = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      user.setUserId(userId);
      return user;

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

  public Users getUserById(int userId) throws SQLException {
    String selectUser = "SELECT * FROM Users WHERE UserId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectUser);
      selectStmt.setInt(1, userId);
      results = selectStmt.executeQuery();

      if (results.next()) {
        int resultUserId = results.getInt("UserId");
        String firstName = results.getString("FirstName");
        String lastName = results.getString("LastName");
        String email = results.getString("Email");
        String password = results.getString("Password");

        Users user = new Users(resultUserId, firstName, lastName, email, password);
        return user;
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

  public Users getUserByEmailAndPassword(String email, String password) throws SQLException {
    String selectUser = "SELECT * FROM Users WHERE Email = ? AND Password = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectUser);
      selectStmt.setString(1, email);
      selectStmt.setString(2, password);
      results = selectStmt.executeQuery();

      if (results.next()) {
        int userId = results.getInt("UserId");
        String firstName = results.getString("FirstName");
        String lastName = results.getString("LastName");
        String resultEmail = results.getString("Email");
        String resultPassword = results.getString("Password");
        Users user = new Users(userId, firstName, lastName, resultEmail, resultPassword);
        return user;
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


  public List<Users> getUsersFromFirstName(String firstName) throws SQLException {
    List<Users> users = new ArrayList<Users>();
    String selectUsers = "SELECT * FROM Users WHERE FirstName = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectUsers);
      selectStmt.setString(1, firstName);
      results = selectStmt.executeQuery();
      while(results.next()) {
        int resultUserId = results.getInt("UserId");
        String resultFirstName = results.getString("FirstName");
        String lastName = results.getString("LastName");
        String email = results.getString("Email");
        String password = results.getString("Password");

        Users user = new Users(resultUserId, resultFirstName, lastName, email, password);
        users.add(user);
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
    return users;
  }

  public Users updateLastName(Users user, String newLastName) throws SQLException {
    String updateUser = "UPDATE Users SET LastName=? WHERE UserId=?;";
    Connection connection = null;
    PreparedStatement updateStmt = null;
    try {
      connection = connectionManager.getConnection();
      updateStmt = connection.prepareStatement(updateUser);
      updateStmt.setString(1, newLastName);
      updateStmt.setInt(2, user.getUserId());
      updateStmt.executeUpdate();

      // Update the person param before returning to the caller.
      user.setLastName(newLastName);
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if(connection != null) {
        connection.close();
      }
      if(updateStmt != null) {
        updateStmt.close();
      }
    }
  }

  public Users delete(Users user) throws SQLException {
    String deleteUser = "DELETE FROM Users WHERE UserId = ?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;

    try {
      connection = connectionManager.getConnection();
      deleteStmt = connection.prepareStatement(deleteUser);
      deleteStmt.setInt(1, user.getUserId());
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
