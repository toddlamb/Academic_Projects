package dal;

import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.groupConcatDistinct;
import static org.jooq.impl.DSL.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.MainPageData;
import model.Restaurants;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;

public class RestaurantsDao {

  protected ConnectionManager connectionManager;
  private static RestaurantsDao instance = null;

  protected RestaurantsDao() {
    connectionManager = new ConnectionManager();
  }

  public static RestaurantsDao getInstance() {
    if (instance == null) {
      instance = new RestaurantsDao();
    }
    return instance;
  }

  public Restaurants create(Restaurants restaurant) throws SQLException {
    String insertRestaurant = "INSERT INTO Restaurants(Name,IsOpen,HasDelivery,TakesReservations) "
        + "VALUES(?,?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;

    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertRestaurant, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setString(1, restaurant.getName());
      insertStmt.setBoolean(2, restaurant.getisOpen());
      insertStmt.setBoolean(3, restaurant.isHasDelivery());
      insertStmt.setBoolean(4, restaurant.isTakesReservations());
      insertStmt.executeUpdate();

      resultKey = insertStmt.getGeneratedKeys();
      int restaurantId = -1;
      if (resultKey.next()) {
        restaurantId = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      restaurant.setRestaurantId(restaurantId);
      return restaurant;
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

  public Restaurants getRestaurantById(int restaurantId) throws SQLException {
    String selectRestaurant = "SELECT * FROM Restaurants WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setInt(1, restaurantId);
      results = selectStmt.executeQuery();

      if (results.next()) {
        int resultRestaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(resultRestaurantId, name, isOpen, hasDelivery,
            takesReservations);
        return restaurant;
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

  public List<Restaurants> getRestaurantByName(String name) throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant = "SELECT * FROM Restaurants WHERE Name = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, name);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String resultName = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, resultName, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
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
    return restaurants;
  }

  public List<MainPageData> getRestaurantsByParams(Map<String, String> params) throws SQLException {
    List<MainPageData> results = new ArrayList<>();
    if (params.isEmpty()) {
      return results;
    }
    Connection connection = null;
    connection = connectionManager.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.MYSQL_5_7);
    SelectQuery<?> query = context.selectQuery();
    query.addSelect(
        field("res.RestaurantId"),
        field("res.Name"),
        field("res.HasDelivery"),
        field("res.TakesReservations"),
        field("addr.FormattedAddress"),
        field("addr.Formatted_PhoneNumber"),
        field("l.GoogleUrl"),
        field("l.YelpUrl"),
        field("cost.PriceRange"),
        groupConcatDistinct(field("c.CuisineType")).as("CuisineTypes"),
        avg(field("fs.InspectionScore",Integer.class)).as("Average_Inspection_Score"),
        avg(field("rvw.Value",Integer.class)).as("Average_Review_Score")
    );
    query.addLimit(50);
    query.addFrom(table("Restaurants").as("res"));
    query.addJoin(table("Addresses").as("addr"),
        field("res.RestaurantId").eq(field("addr.RestaurantId")));
    query.addJoin(table("Cost").as("cost"),
        field("cost.RestaurantId").eq(field("res.RestaurantId")));
    query.addJoin(table("Cuisine").as("c"),
        field("c.RestaurantId").eq(field("res.RestaurantId")));
    query.addJoin(table("FoodSafety").as("fs"), JoinType.LEFT_OUTER_JOIN,
        field("fs.RestaurantId").eq(field("res.RestaurantId")));
    query.addJoin(table("Links").as("l"),
        field("l.RestaurantId").eq(field("res.RestaurantId")));
    query.addJoin(table("OperatingHours").as("oh"),
        field("oh.RestaurantId").eq(field("res.RestaurantId")));
    query.addJoin(table("Reviews").as("rvw"),field("rvw.RestaurantId").eq(field("res.RestaurantId")));
    query.addGroupBy(field("res.RestaurantId"));
    if (params.containsKey("city")) {
      query.addConditions(field("addr.City").eq(params.get("city")));
    }
    if (params.containsKey("state")) {
      query.addConditions(field("addr.State").eq(params.get("state")));
    }
    if (params.containsKey("cuisine")) {
      query.addConditions(field("c.CuisineType").like('%' + params.get("cuisine") + '%'));
    }
    if (params.containsKey("name")) {
      query.addConditions(field("res.Name").like('%'+params.get("name") + '%'));
    }
    if (params.containsKey("isOpen")) {
      query.addConditions(field("res.IsOpen").isTrue());
    }
    if (params.containsKey("sort")) {
      switch (params.get("sort").toLowerCase()) {
        case "name":
          query.addOrderBy(field("res.Name"));
          break;
        case "cost":
          query.addOrderBy(field("cost.PriceRange"));
      }
    }
    ResultSet resultSet = query.fetchResultSet();
    while (resultSet.next()) {
      results.add(
          new MainPageData(
              resultSet.getInt("RestaurantId"),
              resultSet.getString("Name"),
              resultSet.getBoolean("HasDelivery"),
              resultSet.getBoolean(("TakesReservations")),
              resultSet.getString("FormattedAddress"),
              resultSet.getString("Formatted_PhoneNumber"),
              resultSet.getString("GoogleUrl"),
              resultSet.getString("YelpUrl"),
              resultSet.getInt("PriceRange"),
              resultSet.getString("CuisineTypes"),
              resultSet.getDouble("Average_Inspection_Score"),
              resultSet.getDouble("Average_Review_Score")
          )
      );
    }
    resultSet.close();
    connection.close();
    return results;
  }

  public List<Restaurants> getRestaurantFilter(String city, String state, String cuisineType)
      throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant =
        "SELECT RES.RestaurantId,Name,IsOpen,HasDelivery,TakesReservations,City FROM " +
            "(SELECT Restaurants.RestaurantId,Name,IsOpen,HasDelivery,TakesReservations,City " +
            "FROM Restaurants INNER JOIN Addresses ON Restaurants.RestaurantId = Addresses.RestaurantId "
            +
            "WHERE City = ? AND State = ?) AS RES " +
            "INNER JOIN Cuisine ON RES.RestaurantId = Cuisine.RestaurantId WHERE CuisineType = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, city);
      selectStmt.setString(2, state);
      selectStmt.setString(3, cuisineType);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, name, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
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
    return restaurants;
  }

  public List<Restaurants> getRestaurantByCity(String city) throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant =
        "SELECT Restaurants.RestaurantId,Name,IsOpen,HasDelivery,TakesReservations" +
            " FROM Restaurants INNER JOIN Addresses ON Restaurants.RestaurantId = Addresses.RestaurantId"
            +
            "WHERE City = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, city);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, name, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
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
    return restaurants;
  }

  public List<Restaurants> getRestaurantByState(String state) throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant =
        "SELECT Restaurants.RestaurantId,Name,IsOpen,HasDelivery,TakesReservations" +
            " FROM Restaurants INNER JOIN Addresses ON Restaurants.RestaurantId = Addresses.RestaurantId"
            +
            "WHERE State = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, state);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, name, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
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
    return restaurants;
  }

  public List<Restaurants> getRestaurantByCuisine(String cuisine) throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant =
        "SELECT Restaurants.RestaurantId,Name,IsOpen,HasDelivery,TakesReservations" +
            " FROM Restaurants INNER JOIN Cuisine ON Restaurants.RestaurantId = Cuisine.RestaurantId"
            +
            "WHERE CuisineType = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, cuisine);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, name, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
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
    return restaurants;
  }

  public Restaurants delete(Restaurants restaurant) throws SQLException {
    String deleteRestaurant = "DELETE FROM Restaurants WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;

    try {
      connection = connectionManager.getConnection();
      deleteStmt = connection.prepareStatement(deleteRestaurant);
      deleteStmt.setInt(1, restaurant.getRestaurantId());
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
