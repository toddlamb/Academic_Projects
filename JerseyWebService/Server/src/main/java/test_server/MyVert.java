package test_server;


import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import test_server.ServerMetricData.Method;

/**
 * Endpoint dealing with skier statistics on a given day
 */
@Path(Load.MYVERT)
public class MyVert {

  public static final String skier_stats = "SELECT * FROM SkierStats WHERE SkierId = ? AND DayNumber = ?";
  private Gson gson;
  private MessageSender messageSender;

  public MyVert() throws SQLException, IOException, TimeoutException {
    this.gson = new Gson();
    this.messageSender = MessageManager.getInstance().getMessageSender();
  }

  /**
   * @param skierID Id number for skier.
   * @param dayNum Day number for lift record
   * @return Ok status and json data if record exists, and empty json otherwise.
   */
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  public Response getStats(@QueryParam("skierID") String skierID,
      @QueryParam("dayNum") Integer dayNum) {
    Connection connection = null;
    try {
      connection = ConnectionPool.getConnectionPoolSingleton().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(skier_stats);
      preparedStatement.setInt(1, Integer.parseInt(skierID));
      preparedStatement.setInt(2, dayNum);
      long start_time = System.currentTimeMillis();
      ResultSet resultSet = preparedStatement.executeQuery();
      long end_time = System.currentTimeMillis();
      messageSender.sendMessage(new ServerMetricData(Method.GET, (end_time - start_time), true));
      ResultData resultData = null;
      if (resultSet.next()) {
        resultData = new ResultData(
            resultSet.getInt("TotalVertical"),
            resultSet.getInt("LiftRideCount")
        );
      }
      connection.close();
      String json = gson.toJson(resultData);
      return Response.ok(json).build();
    } catch (SQLException e) {
      messageSender.sendMessage(new ServerMetricData(Method.GET,false));
      return Response.serverError().build();
    }

  }

  private static class ResultData {

    private Integer totalVertical;
    private Integer rideCount;

    /**
     * @param totalVertical Total height of all lifts traversed during a day by a skier.
     * @param rideCount The number of lifts ridden by a skier.
     */
    private ResultData(Integer totalVertical, Integer rideCount) {
      this.totalVertical = totalVertical;
      this.rideCount = rideCount;
    }

  }
}

