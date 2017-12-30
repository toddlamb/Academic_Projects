package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Use ConnectionManager to connect to your MySQL database instance.
 */
public class ConnectionManager {

	private final String user = "username";
	private final String password = "password";
	private final String hostName = "bsdsmysql.ciyufwtr63hr.us-west-2.rds.amazonaws.com";
	private final int port= 3306;
	private final String schema = "SkiResort";

	/** Get the connection to the database instance. */
	public Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.put("user", this.user);
			connectionProperties.put("password", this.password);
			connection = DriverManager.getConnection(
			    "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?autoReconnect=true&useSSL=false",
			    connectionProperties);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return connection;
	}

	/** Close the connection to the database instance. */
	public void closeConnection(Connection connection) throws SQLException {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
