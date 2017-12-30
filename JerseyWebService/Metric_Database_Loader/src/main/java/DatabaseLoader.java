import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DatabaseLoader {

  public static final String METRICS = "metrics";
  public static final int RABBIT_MQ_PORT = 5672;
  public static final int MONGO_DB_PORT = 27017;
  private static final String BROKER_IP = "34.214.46.154";
  public static final String QUEUE_NAME = "Metrics_Queue";
  public static final String UTF_8 = "UTF-8";
  public static final String USERNAME = "toddplamb";
  public static final String PASSWORD = "canes66";
  private static MongoClient mongoClient;
  private static MongoDatabase database;
  private static Connection connection;

  public static void main(String[] args) {

    // Set up Mongo Client and register POJO mapping
    mongoClient = new MongoClient("52.88.69.103", MONGO_DB_PORT);
    PojoCodecProvider provider = PojoCodecProvider.builder().register(QueueMessage.class)
        .build();
    CodecRegistry defaultCodecRegistry = MongoClient.getDefaultCodecRegistry();
    CodecRegistry pojoCodecRegistry = fromRegistries(defaultCodecRegistry, fromProviders(provider));
    database = mongoClient.getDatabase(METRICS);
    database = database.withCodecRegistry(pojoCodecRegistry);
    // Set up Client/Consumer for Queueing service.
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(BROKER_IP);
    factory.setPort(RABBIT_MQ_PORT);
    factory.setUsername(USERNAME);
    factory.setPassword(PASSWORD);
    connection = null;
    Channel channel = null;
    while (connection == null || channel == null) {
      try {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      } catch (IOException exception) {
        exception.printStackTrace();
        continue;
      } catch (TimeoutException e) {
        e.printStackTrace();
        continue;
      }
    }
    // Close connections on exit
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        try {
          connection.close();
          mongoClient.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }));
    final Channel finalChannel = channel;
    final Consumer consumer = new DefaultConsumer(finalChannel) {
      /**
       * Handles messages pulled from queue.
       * @param consumerTag
       * @param envelope
       * @param properties
       * @param body JSON representation of Server Metric Data sent from web server.
       * @throws IOException
       */
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
          byte[] body) throws IOException {
        String message = new String(body, UTF_8);
        MongoCollection<QueueMessage> collection = database
            .getCollection(METRICS, QueueMessage.class);
        QueueMessage queueMessage = new Gson().fromJson(message, QueueMessage.class);
        collection.insertOne(queueMessage);
        finalChannel.basicAck(envelope.getDeliveryTag(), false);
      }
    };
    boolean autoAck = false;
    try {
      channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
