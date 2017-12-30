package utilities;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import datamap.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bson.Document;

/**
 * Used to manage the process of calculating metrics for web server to database traffic.
 */
public class ServerMetricsManager {

  public static final String HOST = "52.88.69.103";
  public static final int PORT = 27017;
  public static final String METRICS = "metrics";
  public static final String AVERAGE_LATENCY = "average_latency";
  public static final String $LATENCY = "$latency";
  public static final String $IP_ADDRESS = "$ipAddress";
  public static final String LATENCY = "latency";
  public static final String IP_ADDRESS = "ipAddress";
  public static final String WAS_SUCCESSFUL = "wasSuccessful";
  public static final String ID = "_id";
  private MongoClient mongoClient;
  private MongoDatabase database;
  private MongoCollection<Document> collection;
  public static final String[] ADDRESSES = new String[]{"34.214.158.247", "35.165.235.81",
      "35.162.187.160"};

  public ServerMetricsManager() {
    this.mongoClient = new MongoClient(HOST, PORT);
    database = mongoClient.getDatabase(METRICS);
    this.collection = database.getCollection(METRICS);
    addHook();
  }

  /**
   * Closes database connection on shutdown
   */
  private void addHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        clearCollection();
        mongoClient.close();
      }
    }));
  }

  /**
   * Deletes all server metrics in database.
   */
  public void clearCollection() {
    this.collection.deleteMany(new Document());
  }

  /**
   * Close database connection before garbage collection.
   */
  @Override
  protected void finalize() throws Throwable {
    clearCollection();
    mongoClient.close();
    super.finalize();
  }

  /**
   * @return Mean latency for all server traffic.
   */
  public Long getMean() {
    AggregateIterable<Document> result =
        collection.aggregate(
            Arrays.asList(
                Aggregates.group(null, Accumulators.avg(AVERAGE_LATENCY, $LATENCY))
            )
        );
    Double average = result.first().getDouble(AVERAGE_LATENCY);
    return Math.round(average);
  }

  /**
   * @return Percentage of total server traffic that successfully completed.
   */
  public double getSuccessRate() {
    Long successCount = collection.count(Filters.eq(WAS_SUCCESSFUL, true));
    Long totalCount = collection.count();
    return successCount.doubleValue() / totalCount.doubleValue() * 100;
  }

  /**
   * @return A map of server addresses to success rate of given server traffic.
   */
  public Map<String, Double> getSuccessRateByServer() {
    Map<String, Double> results = new HashMap<>();
    for (String address : ADDRESSES) {
      Long successCount = collection
          .count(Filters.and(Filters.eq(WAS_SUCCESSFUL, true), Filters.eq(IP_ADDRESS, address)));
      Long totalCount = collection.count(Filters.eq(IP_ADDRESS, address));
      results.put(address,
          (successCount.doubleValue() / totalCount.doubleValue()) * 100
      );
    }
    return results;
  }

  /**
   * @return A map of server addresses to mean server/database traffic latency.
   */
  public Map<String, Double> getMeanByIp() {
    Map<String, Double> results = new HashMap<>();
    collection.aggregate(
        Arrays.asList(
            Aggregates.group($IP_ADDRESS, Accumulators.avg(AVERAGE_LATENCY, $LATENCY))
        )
    ).forEach(new Consumer<Document>() {
      @Override
      public void accept(Document document) {
        results.put(
            document.getString(ID),
            document.getDouble(AVERAGE_LATENCY)
        );
      }
    });
    return results;
  }

  /**
   * @return Median server/database traffic latency for all servers.
   */
  public Long getMedian() {
    Long recordCount = collection.count();
    List<Document> documents = new ArrayList<>();
    collection.find().sort(Sorts.ascending($LATENCY)).into(documents);
    if (documents.size() % 2 == 0) {
      return (documents.get(recordCount.intValue() / 2).getLong(LATENCY) + documents
          .get((recordCount.intValue() / 2) + 1).getLong(LATENCY)) / 2;
    } else {
      return documents.get(recordCount.intValue() / 2).getLong(LATENCY);
    }
  }

  /**
   * Gets middle index of a list
   */
  private static int getMiddleIndex(List<Document> list) {
    int length = list.size();
    int mid = length / 2;
    if (mid < length) {
      return mid;
    } else {
      return length - 1;
    }
  }

  /**
   * Gets the 2 centermost indexes of a list containing an even number of elements.
   */
  private static Pair<Integer, Integer> getMiddleIndexes(List<Document> list) {
    int length = list.size();
    int mid = length / 2;
    int secondMid = mid + 1;
    Pair<Integer, Integer> result = new Pair<>();
    result.setFirst(
        mid < length ? mid : length - 1
    );
    result.setSecond(
        secondMid < length ? secondMid : length - 1
    );
    return result;
  }


  /**
   * @return A map of server addresses to median server/database latency by server.
   */
  public Map<String, Long> getMedianByIp() {
    Map<String, Long> results = new HashMap<>();
    List<Document> documentList;
    int length;
    Long median;
    for (String address : ADDRESSES) {
      documentList = new ArrayList<>();
      collection.find(Filters.eq(IP_ADDRESS, address)).sort(Sorts.ascending($LATENCY))
          .into(documentList);
      length = documentList.size();
      if (length % 2 == 0) {
        Pair<Integer, Integer> indexes = getMiddleIndexes(documentList);
        median =
            (documentList.get(indexes.getFirst()).getLong(LATENCY) + documentList
                .get(indexes.getSecond())
                .getLong(LATENCY)) / 2;
        results.put(address, median);
      } else {
        Integer index = getMiddleIndex(documentList);
        median = documentList.get(index).getLong(LATENCY);
        results.put(address, median);
      }
    }
    return results;
  }

  /**
   * @param percentile The desired percentile value of the list.
   * @return The nth percentile latency for all servers.
   */
  public Long getPercentile(float percentile) {
    List<Document> documents = new ArrayList<>();
    collection.find().sort(Sorts.ascending(LATENCY)).into(documents);
    int percentileIndex = new Float(Math.floor(documents.size() * percentile)).intValue();
    return percentileIndex < documents.size() ? documents.get(percentileIndex).getLong(LATENCY)
        : documents.get(documents.size() - 1).getLong(LATENCY);
  }

  /**
   * @param percentile The desired percentile value of the list.
   * @return A map of server addresses to the nth percentile latency for each server.
   */
  public Map<String, Long> getPercentileByIp(float percentile) {
    Map<String, Long> results = new HashMap<>();
    List<Document> documents = null;
    for (String address : ADDRESSES) {
      documents = new ArrayList<>();
      collection.find(Filters.eq(IP_ADDRESS, address)).sort(Sorts.ascending($LATENCY))
          .into(documents);
      int percentileIndex = new Float(Math.floor(documents.size() * percentile)).intValue();
      results.put(address,
          percentileIndex < documents.size() ? documents.get(percentileIndex).getLong(LATENCY)
              : documents.get(documents.size() - 1).getLong(LATENCY)
      );
    }
    return results;
  }

}


