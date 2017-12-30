package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Addresses;
import model.Restaurants;

public class AddressesDao {
	protected ConnectionManager connectionManager;
	private static AddressesDao instance = null;

	protected AddressesDao() {
		connectionManager = new ConnectionManager();
	}

	public static AddressesDao getInstance() {
		if (instance == null) {
			instance = new AddressesDao();
		}
		return instance;
	}

	public Addresses create(Addresses addresses) throws SQLException {
		String sqlTemplate = "INSERT INTO Addresses(RestaurantId,FormattedAddress,Latitude, Longitude, StreetOne, StreetTwo," +
				"City, state, country, ZipCode, Formatted_PhoneNumber, International_PhoneNumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = connectionManager.getConnection();
			preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, addresses.getRestaurant().getRestaurantId());
			preparedStatement.setString(2, addresses.getFormattedAddress());
			preparedStatement.setDouble(3, addresses.getLatitude());
			preparedStatement.setDouble(4, addresses.getLongitude());
			preparedStatement.setString(5, addresses.getStreetOne());
			preparedStatement.setString(6, addresses.getStreetTwo());
			preparedStatement.setString(7, addresses.getCity());
			preparedStatement.setString(8, addresses.getState());
			preparedStatement.setString(9, addresses.getCountry());
			preparedStatement.setString(10, addresses.getZipCode());
			preparedStatement.setString(11, addresses.getFormattedPhoneNumber());
			preparedStatement.setString(12, addresses.getInternationalPhoneNumber());
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				addresses.setAddressId(resultSet.getInt(1));
			}
			return addresses;
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


	public Addresses getAddressById(int addressId) throws SQLException {
		String selectAddress = "SELECT * FROM Address WHERE AddressId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectAddress);
			selectStmt.setInt(1, addressId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			if (results.next()) {
				int resultAddressId = results.getInt("AddressId");
				int restaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(restaurantId);
				String formattedAddress = results.getString("FormattedAddress");
				double latitude = results.getDouble("Latitude");
				double longitude = results.getDouble("Longitude");
				String streetOne = results.getString("StreetOne");
				String streetTwo = results.getString("StreetTwo");
				String city = results.getString("City");
				String state = results.getString("State");
				String country = results.getString("Country");
				String zipCode = results.getString("ZipCode");
				String formattedPhoneNumber = results.getString("FormattedPhoneNumber");
				String internationalPhoneNumber = results.getString("InternationalPhoneNumber");
				Addresses address = new Addresses(resultAddressId, restaurant, formattedAddress, latitude, longitude,
						streetOne, streetTwo, city, state, country, zipCode, formattedPhoneNumber,
						internationalPhoneNumber);
				return address;
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

	public List<Addresses> getAddressesByRestaurantId(int restaurantId) throws SQLException {
		List<Addresses> list = new ArrayList<Addresses>();
		String selectAddress = "SELECT * FROM Addresses WHERE RestaurantId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectAddress);
			selectStmt.setInt(1, restaurantId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			while (results.next()) {
				int addressId = results.getInt("AddressId");
				int resultRestaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
				String formattedAddress = results.getString("FormattedAddress");
				double latitude = results.getDouble("Latitude");
				double longitude = results.getDouble("Longitude");
				String streetOne = results.getString("StreetOne");
				String streetTwo = results.getString("StreetTwo");
				String city = results.getString("City");
				String state = results.getString("State");
				String country = results.getString("Country");
				String zipCode = results.getString("ZipCode");
				String formattedPhoneNumber = results.getString("Formatted_PhoneNumber");
				String internationalPhoneNumber = results.getString("International_PhoneNumber");
				Addresses address = new Addresses(addressId, restaurant, formattedAddress, latitude, longitude,
						streetOne, streetTwo, city, state, country, zipCode, formattedPhoneNumber,
						internationalPhoneNumber);
				list.add(address);
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

	public Addresses delete(Addresses address) throws SQLException {
		String deleteAddresses = "DELETE FROM Addresses WHERE AddressId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteAddresses);
			deleteStmt.setInt(1, address.getAddressId());
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
