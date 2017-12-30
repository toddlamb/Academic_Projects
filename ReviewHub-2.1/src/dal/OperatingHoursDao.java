package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.OperatingHours;
import model.Restaurants;

public class OperatingHoursDao {
	protected ConnectionManager connectionManager;
	private static OperatingHoursDao instance = null;

	protected OperatingHoursDao() {
		connectionManager = new ConnectionManager();
	}

	public static OperatingHoursDao getInstance() {
		if (instance == null) {
			instance = new OperatingHoursDao();
		}
		return instance;
	}

	public OperatingHours create(OperatingHours opHour) throws SQLException {
		String insertOperatingHour = "INSERT INTO OperatingHours (RestaurantId, DayOfWeek, StartTime, endTime) "
				+ "VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;

		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertOperatingHour);
			insertStmt.setInt(1, opHour.getRestaurant().getRestaurantId());
			insertStmt.setString(2, opHour.getDay().name());
			insertStmt.setTimestamp(3, new Timestamp(opHour.getStartTime().getTime()));
			insertStmt.setTimestamp(4, new Timestamp(opHour.getStartTime().getTime()));

			resultKey = insertStmt.getGeneratedKeys();
			int opHourId = -1;
			if (resultKey.next()) {
				opHourId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			opHour.setOperatingHourId(opHourId);
			return opHour;
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

	public OperatingHours getOperatingHourById(int operatingHoursId) throws SQLException {
		String selectOperatingHour = "SELECT * FROM OperatingHours WHERE OperatingHoursId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectOperatingHour);
			selectStmt.setInt(1, operatingHoursId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			if (results.next()) {
				int resultOperatingHoursId = results.getInt("OperatingHoursId");
				int restaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(restaurantId);
				OperatingHours.DayOfWeek day = OperatingHours.DayOfWeek.valueOf(results.getString("DayOfWeek"));
				Date startTime = new Date(results.getTimestamp("Start").getTime());
				Date endTime = new Date(results.getTimestamp("Start").getTime());
				OperatingHours operatingHour = new OperatingHours(resultOperatingHoursId, restaurant, day, startTime,
						endTime);
				return operatingHour;
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

	public List<OperatingHours> getOperatingHoursByRestaurantId(int restaurantId) throws SQLException {
		List<OperatingHours> list = new ArrayList<OperatingHours>();
		String selectOperatingHour = "SELECT * FROM OperatingHours WHERE RestaurantId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectOperatingHour);
			selectStmt.setInt(1, restaurantId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			while (results.next()) {
				int operatingHoursId = results.getInt("OperatingHoursId");
				int resultRestaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
				OperatingHours.DayOfWeek day = OperatingHours.DayOfWeek.valueOf(results.getString("DayOfWeek"));
				Date startTime = new Date(results.getTimestamp("Start").getTime());
				Date endTime = new Date(results.getTimestamp("Start").getTime());
				OperatingHours operatingHour = new OperatingHours(operatingHoursId, restaurant, day, startTime,
						endTime);
				list.add(operatingHour);
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
		return list;
	}
	
	public OperatingHours delete(OperatingHours operatingHour) throws SQLException {
		String deleteOperatingHour = "DELETE FROM OperatingHours WHERE OperatingHoursId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteOperatingHour);
			deleteStmt.setInt(1, operatingHour.getOperatingHourId());
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
