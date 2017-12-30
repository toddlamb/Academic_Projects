package test_server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Manages connections to the queueing service.
 */
public class MessageManager {

  //private static final String BROKER_IP = "localhost";
  private static final String BROKER_IP = "34.214.46.154";
  public static final String QUEUE_NAME = "Metrics_Queue";
  public static final String DEFAULT_EXCHANGE = "";
  public static final Integer PORT = 5672;
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  private ConnectionFactory connectionFactory;
  private Connection connection;
  private static MessageManager messageManager;


  public static MessageManager getInstance() throws IOException, TimeoutException {
    if (messageManager == null) {
      messageManager = new MessageManager();
    }
    return messageManager;
  }

  private MessageManager() throws IOException, TimeoutException {
    connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(BROKER_IP);
    connectionFactory.setPort(PORT);
    connectionFactory.setUsername(USERNAME);
    connectionFactory.setPassword(PASSWORD);
    connection = connectionFactory.newConnection();
  }

  public MessageSender getMessageSender() throws IOException {
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    MessageSender messageSender = new MessageSender(channel);
    return messageSender;
  }

  @Override
  protected void finalize() throws Throwable {
    this.connection.close();
    super.finalize();
  }


}
