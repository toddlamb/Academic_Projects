package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Links;
import model.Restaurants;

public class LinksDao {
	protected ConnectionManager connectionManager;
	private static LinksDao instance = null;

	protected LinksDao() {
		connectionManager = new ConnectionManager();
	}

	public static LinksDao getInstance() {
		if (instance == null) {
			instance = new LinksDao();
		}
		return instance;
	}

	public Links create(Links link) throws SQLException {
		String insertLinks = "INSERT INTO Addresses(RestaurantId, GoogleUrl, YelpUrl, RestaurantUrl) "
				+ "VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;

		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertLinks);
			insertStmt.setInt(1, link.getRestaurant().getRestaurantId());
			insertStmt.setString(2, link.getGoogleUrl());
			insertStmt.setString(3, link.getYelpUrl());
			insertStmt.setString(4, link.getYelpUrl());

			resultKey = insertStmt.getGeneratedKeys();
			int linkId = -1;
			if (resultKey.next()) {
				linkId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			link.setLinkId(linkId);
			return link;
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
		
	public Links getAddressById(int linkId) throws SQLException {
		String selectLinks = "SELECT * FROM Links WHERE LinkId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectLinks);
			selectStmt.setInt(1, linkId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
			
			if (results.next()) {
				int resultLinksId = results.getInt("LinkId");
				int restaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(restaurantId);
				String googleUrl = results.getString("GoogleUrl");
				String yelpUrl = results.getString("YelpUrl");
				String restaurantUrl = results.getString("RestaurantUrl");
				Links link = new Links(resultLinksId, restaurant, googleUrl, yelpUrl, restaurantUrl);
				return link;
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
	
	public List<Links> getLinksByRestaurantId(int restaurantId) throws SQLException {
		List<Links> list = new ArrayList<Links>();
		String selectLinks = "SELECT * FROM Links WHERE RestaurantId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectLinks);
			selectStmt.setInt(1, restaurantId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();
			
			while (results.next()) {
				int linksId = results.getInt("LinkId");
				int resultRestaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
				String googleUrl = results.getString("GoogleUrl");
				String yelpUrl = results.getString("YelpUrl");
				String restaurantUrl = results.getString("RestaurantUrl");
				Links link = new Links(linksId, restaurant, googleUrl, yelpUrl, restaurantUrl);
				list.add(link);
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
	
	public Links delete(Links link) throws SQLException {
		String deleteReservation = "DELETE FROM Links WHERE LinkId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteReservation);
			deleteStmt.setInt(1, link.getLinkId());
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
