package test_server;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import test_server.ServerMetricData.Method;

/**
 * Endpoint utilizing lift data from RFID Scanner.
 */
@Path(Load.LOAD)
public class Load {

  private static final String sql_template = "INSERT INTO LiftData(ResortID,DayNumber,Timestamp,SkierId,LiftId) VALUES(?,?,?,?,?);";
  public static final String MYVERT = "myvert";
  public static final String LOAD = "load";
  private Connection connection;
  private Gson gson;
  private PreparedStatement preparedStatement;
  private MessageSender messageSender;

  public Load() throws SQLException, IOException, TimeoutException {
    this.connection = ConnectionPool.getConnectionPoolSingleton().getConnection();
    this.gson = new Gson();
    this.preparedStatement = connection.prepareStatement(sql_template);
    this.messageSender = MessageManager.getInstance().getMessageSender();
  }


  /**
   * Adds lift data to the database
   *
   * @param json Lift data stored as json
   * @return Created status if record was added, Unacceptable if misformatted, and 500 upon DB
   * error.
   * @throws SQLException Upon database error.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postText(String json) throws SQLException {
    if (json == null || json.length() == 0) {
      return Response.notAcceptable(Variant.VariantListBuilder.newInstance().build()).build();
    }
    LiftInfo liftInfo = gson.fromJson(json, LiftInfo.class);
    if (!liftInfo.isValid()) {
      return Response.notAcceptable(Variant.VariantListBuilder.newInstance().build()).build();
    }
    try {
      preparedStatement.setInt(1, Integer.parseInt(liftInfo.getResortID()));
      preparedStatement.setInt(2, liftInfo.getDayNum());
      preparedStatement.setInt(3, Integer.parseInt(liftInfo.getTime()));
      preparedStatement.setInt(4, Integer.parseInt(liftInfo.getSkierID()));
      preparedStatement.setInt(5, Integer.parseInt(liftInfo.getLiftID()));
      long start_time = System.currentTimeMillis();
      preparedStatement.executeUpdate();
      long end_time = System.currentTimeMillis();
      connection.close();
      messageSender.sendMessage(new ServerMetricData(Method.POST, (end_time - start_time), true));
      return Response.created(URI.create(MYVERT)).build();
    } catch (SQLException exc) {
      messageSender.sendMessage(new ServerMetricData(Method.POST, false));
      if (connection != null) {
        connection.close();
      }
      return Response.serverError().build();
    }
  }

}

