package test_server;

import static test_server.MessageManager.DEFAULT_EXCHANGE;
import static test_server.MessageManager.QUEUE_NAME;

import com.google.gson.Gson;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import com.rabbitmq.client.Channel;

/**
 * Used for sending messages to queueing service
 */
public class MessageSender {

  private Channel channel;

  public MessageSender(Channel channel) {
    this.channel = channel;
  }

  @Override
  protected void finalize() throws Throwable {
    this.channel.close();
    super.finalize();
  }

  /**
   * Sends message to queueing service.
   * @param message The message to be sent as an object.
   */
  public void sendMessage(Object message) {
    Gson gson = new Gson();
    String envelope = gson.toJson(message);
    try {
      this.channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_NAME, MessageProperties.TEXT_PLAIN,
          envelope.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
