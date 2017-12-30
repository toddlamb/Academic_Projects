package test_server;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Represents a pre-configured jdbc connection pool singleton.
 */
public class ConnectionPool {

  private static final int THIRTY_SECONDS = 30000;
  private static final int TEN_SECONDS = 10000;
  private static final int TIMEOUT = 60;
  private static final int MIN_IDLE = 10;
  private static final String HOST = "bsdsmysql.ciyufwtr63hr.us-west-2.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private static final int MAX_ACTIVE = 20;
  private static final int INITIAL_SIZE = 10;
  private static final String JDBC_INTERCEPTORS =
      "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
          + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer";
  private static final String MY_SQL_PROTOCOL = "jdbc:mysql://";
  private static final String DATABASE = "SkiResort";
  private DataSource dataSource;
  private static ConnectionPool connectionPool = new ConnectionPool();

  /**
   * Singleton constructor for pool
   *
   * @return The connection pool.
   */
  public static ConnectionPool getConnectionPoolSingleton() {
    return connectionPool;
  }

  /**
   * Creates the connection pool with hard-coded settings at run-time.
   */
  private ConnectionPool() {
    PoolProperties poolProperties = new PoolProperties();
    poolProperties.setUrl(getUrl());
    poolProperties.setDriverClassName(DRIVER_CLASS_NAME);
    poolProperties.setUsername(USERNAME);
    poolProperties.setPassword(PASSWORD);
    poolProperties.setJmxEnabled(true);
    poolProperties.setTestWhileIdle(false);
    poolProperties.setTestOnBorrow(true);
    poolProperties.setValidationQuery("SELECT 1");
    poolProperties.setTestOnReturn(false);
    poolProperties.setValidationInterval(THIRTY_SECONDS);
    poolProperties.setTimeBetweenEvictionRunsMillis(THIRTY_SECONDS);
    poolProperties.setMaxActive(MAX_ACTIVE);
    poolProperties.setInitialSize(INITIAL_SIZE);
    poolProperties.setMaxWait(TEN_SECONDS);
    poolProperties.setRemoveAbandonedTimeout(TIMEOUT);
    poolProperties.setMinEvictableIdleTimeMillis(THIRTY_SECONDS);
    poolProperties.setMinIdle(MIN_IDLE);
    poolProperties.setLogAbandoned(true);
    poolProperties.setRemoveAbandoned(true);
    poolProperties.setJdbcInterceptors(JDBC_INTERCEPTORS);
    this.dataSource = new DataSource();
    dataSource.setPoolProperties(poolProperties);
  }

  /**
   * Creates url for RDS database.
   *
   * @return url for RDS database.
   */
  private static String getUrl() {
    return MY_SQL_PROTOCOL + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&user=" + USERNAME
        + "&password=" + PASSWORD;
  }

  /**
   * Attempts to get a jdbc connection from the pool if one is available.
   *
   * @throws SQLException If database error occurs.
   * @returna jdbc connection from the pool if one is available.
   */
  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }


  /**
   * Closes database connections on pool destruction.
   */
  @Override
  public void finalize() throws Throwable {
    this.dataSource.close(true);
    super.finalize();
  }

}
