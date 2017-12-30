import bsdsass2testdata.RFIDLiftData;
import com.google.gson.Gson;
import datamap.IntPair;
import datamap.MetricPair;
import datamap.Pair;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import utilities.AtomicRequestTracker;
import utilities.CommandValidator;
import utilities.CommandValidator.Method;
import utilities.ConnectionManager;
import utilities.LineChart;
import utilities.SerializedDataManager;
import utilities.ServerMetricsManager;
import utilities.StatCalculator;

/**
 * Web client used for sending multi-threaded get and post requests.
 */
public class Web_Client {

  private static final double MILLISEC_PER_SEC = 1000.00;
  public static final String GET_PATHNAME = "Get_LineChart.jpeg";
  public static final String APPLICATION_TITLE = "Homework 3";
  public static final String CHART_TITLE = "Latency over time";
  public static final String POST_PATHNAME = "Post_LineChart.jpeg";
  public static final String SEQUENTIAL_PATHNAME = "Sequential_LineChart.jpeg";
  private static CyclicBarrier barrier;
  private static CountDownLatch latch;
  private static AtomicRequestTracker tracker;
  private static long test_start_time;
  private static final String FILE_LOCATION = "BSDSAssignment2Day3.ser";
  private static final String[] FILE_LOCATIONS = {"BSDSAssignment2Day3.ser",
      "BSDSAssignment2Day4.ser", "BSDSAssignment2Day5.ser"};
  private static final int SKIER_COUNT = 40000;
  private static AtomicLong test_end_time = new AtomicLong();

  /**
   * Starts the timer for a request
   */
  private static void start_client_time() {
    test_start_time = System.currentTimeMillis();
    System.out.println("Client starting....Time: " + test_start_time);
  }

  /**
   * Executes requests in a manner supplied by the command line arguments
   *
   * @param args Specifies client behavior including thread count, host, port, and method
   * @throws IllegalArgumentException If command line validator finds arguments are incorrectly
   * formatted or otherwise invalid
   */
  public static void main(String[] args)
      throws IllegalArgumentException, SQLException, InterruptedException {
    //Validate command line arguments and populate for consumption
    CommandValidator commands = new CommandValidator(args);
    if (!commands.isValid()) {
      throw new IllegalArgumentException(
          "One or more arguments is invalid or improperly formatted");
    }
    System.out.println("Web_Client " + commands.getThreadCount()
        + " " + commands.getPort() + " " + commands.getIpAddress());
    try {
      //Set up cross thread count keeper
      tracker = new AtomicRequestTracker();
      ExecutorService executorService = Executors.newFixedThreadPool(commands.getThreadCount() + 1);
      List<Future<List<MetricPair>>> results = new ArrayList<>(commands.getThreadCount() * 6);
      //Run threads and add futures to list
      SerializedDataManager dataManager = null;
      List<List<RFIDLiftData>> skiDataList = null;
      int skiers_per_thread;
      switch (Method.values()[commands.getMethod()]) {
        case POST:
          latch = new CountDownLatch(commands.getThreadCount());
          executorService.submit(new Runnable() {
            @Override
            public void run() {
              try {
                latch.await();
                test_end_time.set(System.currentTimeMillis());
                double runtime = (test_end_time.get() - test_start_time) / MILLISEC_PER_SEC;
                System.out.println("All threads complete... Time: " + test_end_time);
                System.out.println("Total number of requests sent: " + tracker.getRequestCount());
                System.out
                    .println("Total number of successful responses: " + tracker.getSuccessCount());
                System.out.println("Test Wall Time: " + runtime + " seconds");
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
          dataManager = new SerializedDataManager(new File(FILE_LOCATION));
          skiDataList = dataManager.split_list(commands.getThreadCount());
          start_client_time();
          for (int i = 0; i < commands.getThreadCount(); i++) {
            Future<List<MetricPair>> calculation = executorService.submit(
                new PostThreadTask(skiDataList.get(i), commands.getPort(), commands.getIpAddress())
            );
            results.add(calculation);
          }
          break;
        case GET:
          latch = new CountDownLatch(commands.getThreadCount());
          executorService.submit(new Runnable() {
            @Override
            public void run() {
              try {
                latch.await();
                test_end_time.set(System.currentTimeMillis());
                double runtime = (test_end_time.get() - test_start_time) / MILLISEC_PER_SEC;
                System.out.println("All threads complete... Time: " + test_end_time);
                System.out.println("Total number of requests sent: " + tracker.getRequestCount());
                System.out
                    .println("Total number of successful responses: " + tracker.getSuccessCount());
                System.out.println("Test Wall Time: " + runtime + " seconds");
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
          skiers_per_thread = (SKIER_COUNT / commands.getThreadCount());
          start_client_time();
          for (int i = 1; i < SKIER_COUNT; i += skiers_per_thread) {
            Future<List<MetricPair>> calculation = executorService.submit(
                new GetThreadTask(commands.getPort(), commands.getIpAddress(), i,
                    i + skiers_per_thread)
            );
            results.add(calculation);
          }
          break;
        case SEQUENTIAL:
          int latch_size = commands.getThreadCount() * 6;
          latch = new CountDownLatch(latch_size);
          executorService.submit(new Runnable() {
            @Override
            public void run() {
              try {
                latch.await();
                test_end_time.set(System.currentTimeMillis());
                double runtime = (test_end_time.get() - test_start_time) / MILLISEC_PER_SEC;
                System.out.println("All threads complete... Time: " + test_end_time);
                System.out.println("Total number of requests sent: " + tracker.getRequestCount());
                System.out
                    .println("Total number of successful responses: " + tracker.getSuccessCount());
                System.out.println("Test Wall Time: " + runtime + " seconds");
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
          // Establish connection to database for updating statistics
          ConnectionManager connectionManager = new ConnectionManager();
          String sql = "CALL load_statistics(?)";
          Connection connection = null;
          PreparedStatement preparedStatement;
          int daynum;
          start_client_time();
          int threshold = latch_size - commands.getThreadCount();
          for (String file : FILE_LOCATIONS) {
            // Run post requests for day
            dataManager = new SerializedDataManager(new File(file));
            skiDataList = dataManager.split_list(commands.getThreadCount());
            for (int i = 0; i < commands.getThreadCount(); i++) {
              Future<List<MetricPair>> calculation = executorService.submit(
                  new PostThreadTask(skiDataList.get(i), commands.getPort(),
                      commands.getIpAddress())
              );
              results.add(calculation);
            }
            while (latch.getCount() > threshold) {
              // Wait for posts to finish before moving on
            }
            threshold -= commands.getThreadCount();
            //Update statistics when posts finish
            daynum = GetThreadTask.parseDayNumber(file);
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, daynum);
            preparedStatement.executeUpdate();
            connection.close();
            skiers_per_thread = (SKIER_COUNT / commands.getThreadCount());
            for (int i = 1; i < SKIER_COUNT; i += skiers_per_thread) {
              Future<List<MetricPair>> calculation = executorService.submit(
                  new GetThreadTask(commands.getPort(), commands.getIpAddress(), i,
                      i + skiers_per_thread, daynum)
              );
              results.add(calculation);
            }
            while (latch.getCount() > threshold) {
              // Wait for gets to finish before moving on
            }
            threshold -= commands.getThreadCount();
          }
          break;
      }
      executorService.shutdown();
      //Calculate post-test statistics
      System.out.println("All threads running...");
      executorService.awaitTermination(30, TimeUnit.MINUTES);
      StatCalculator calculator = new StatCalculator(results, commands, tracker, test_start_time,
          test_end_time.get());
      System.out.printf("Mean Latency: %d\n", calculator.getMean());
      System.out.printf("Median Latency: %d\n", calculator.getMedian());
      System.out.printf("99th Percentile: %d\n", calculator.getPercentile(99));
      System.out.printf("95th Percentile: %d\n", calculator.getPercentile(95));
      System.out.printf("Throughput: %d\n\n", calculator.getThroughPut());
      ServerMetricsManager serverMetricsManager = new ServerMetricsManager();
      System.out.println("\n\nServer Mean Latency: " + serverMetricsManager.getMean());
      System.out.println("Server Mean Latency by Server: " + serverMetricsManager.getMeanByIp());
      System.out.println("Server Median Latency: " + serverMetricsManager.getMedian());
      System.out
          .println("Server Median Latency by Server: " + serverMetricsManager.getMedianByIp());
      System.out.println("Server 95th percentile: " + serverMetricsManager.getPercentile(0.95f));
      System.out.println(
          "Server 95th percentile by Server: " + serverMetricsManager.getPercentileByIp(0.95f));
      System.out.println("Server 99th percentile: " + serverMetricsManager.getPercentile(0.99f));
      System.out.println(
          "Server 99th percentile by Server: " + serverMetricsManager.getPercentileByIp(0.99f));
      System.out.println("Server success rate: " + serverMetricsManager.getSuccessRate());
      System.out.println(
          "Server success rate by server: " + serverMetricsManager.getSuccessRateByServer());
      serverMetricsManager.clearCollection();
      // Generate line chart for results
      LineChart lineChart;
      switch (Method.values()[commands.getMethod()]) {
        case POST:
          lineChart = new LineChart(APPLICATION_TITLE, CHART_TITLE, calculator.getCalculations(),
              POST_PATHNAME);
          break;
        case GET:
          lineChart = new LineChart(APPLICATION_TITLE, CHART_TITLE,
              calculator.getCalculations(), GET_PATHNAME);
          break;
        case SEQUENTIAL:
          lineChart = new LineChart(APPLICATION_TITLE, CHART_TITLE, calculator.getCalculations(),
              SEQUENTIAL_PATHNAME);
          break;
      }
    } catch (ClassNotFoundException except) {
      except.printStackTrace();
    } catch (IOException except) {
      except.printStackTrace();
    }

  }

  /**
   * Abstracted thread object with common behaviour for get and post request threads.
   */
  protected static abstract class AbstractThreadTask implements Callable {

    protected static final String CACHE_CONTROL = "Cache-Control";
    protected static final String CACHE_POLICY = "public";
    protected static final String HTTP = "";
    protected static final Integer CREATED = 201;
    protected static final Integer OK = 200;
    protected static final String ROOT = "/homework_3/rest/";
    protected Integer port;
    protected String ipAddress;
    protected long start_time;
    protected long end_time;
    protected Response response;
    protected int success = 0;
    protected int failure = 0;
    protected List<MetricPair> results;
    protected Gson gson = new Gson();

    protected AbstractThreadTask(Integer port, String ipAddress) {
      this.port = port;
      this.ipAddress = ipAddress;
    }

    /**
     * Creates a request builder using pre-configured headers and data from command line arguments.
     *
     * @param resource Resouce endpoint being requested
     * @param acceptMedia Type of response data accepted
     * @return a request builder using pre-configured headers and data from command line arguments.
     */
    protected Builder createRequest(String resource, String acceptMedia) {
      Client client = JerseyClientBuilder.newClient(new ClientConfig());
      WebTarget rootEndpoint = client.target(HTTP + ipAddress + ":" + port);
      WebTarget resourceEndpoint = rootEndpoint.path(resource);
      return resourceEndpoint.request(acceptMedia).header(CACHE_CONTROL, CACHE_POLICY);
    }

    /**
     * a request builder using pre-configured headers and data from command line arguments.
     *
     * @param resource Resouce endpoint being requested
     * @param acceptMedia Type of response data accepted
     * @param params Query parameters supplied as json.
     */
    protected Builder createRequest(String resource, String acceptMedia,
        List<Pair<String, Integer>> params) {
      Client client = JerseyClientBuilder.newClient(new ClientConfig());
      WebTarget rootEndpoint = client.target(HTTP + ipAddress + ":" + port);
      WebTarget resourceEndpoint = rootEndpoint.path(resource);
      for (Pair<String, Integer> param : params) {
        resourceEndpoint = resourceEndpoint.queryParam(param.getFirst(), param.getSecond());
      }
      return resourceEndpoint.request(acceptMedia).header(CACHE_CONTROL, CACHE_POLICY);
    }


    /**
     * send local results to main counter
     */
    protected void log_transactions() {
      tracker.trackRequest(true, success);
      tracker.trackRequest(false, failure);
    }

    /**
     * Has a thread wait for a barrier to trip.
     *
     * @param thread_barrier Specified barrier to wait for.
     */
    protected void wait_for_threads(CyclicBarrier thread_barrier) {
      try {
        thread_barrier.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (BrokenBarrierException e) {
        e.printStackTrace();
      }
    }

    protected void start_clock() {
      start_time = System.currentTimeMillis();
    }

    protected void stop_clock() {
      end_time = System.currentTimeMillis();
    }

    /**
     * Logs whether or not the request was successful and metrics for the request.
     *
     * @param success_status HTTP response code indicating successful request.
     */
    protected void log_response(int success_status) {
      if (response.getStatus() == success_status) {
        success++;
        results.add(new MetricPair(start_time, end_time));
      } else {
        failure++;
      }
      response.close();
    }

  }

  /**
   * Thread task that submits post requests using a list of data
   */
  private static class PostThreadTask extends AbstractThreadTask {

    private static final String LOAD = "load";
    private static final String TARGET_RESOURCE = "homework_3/rest/" + LOAD;
    //private static final String TARGET_RESOURCE = ROOT + LOAD;
    private List<RFIDLiftData> liftData;

    private PostThreadTask(List<RFIDLiftData> dataList, Integer port, String ipAddress) {
      super(port, ipAddress);
      this.liftData = dataList;
      this.results = new ArrayList<>(dataList.size());
    }

    /**
     * Makes multiple requests using liftData.
     *
     * @return List of metric data
     */
    public Object call() throws Exception {
      //Send requests
      for (RFIDLiftData dataItem : this.liftData) {
        //POST
        try {
          String jsonData = gson.toJson(dataItem);
          start_clock();
          response = createRequest(TARGET_RESOURCE, MediaType.TEXT_PLAIN)
              .post(Entity.entity(jsonData, MediaType.APPLICATION_JSON));
          stop_clock();
          //Track Results
          log_response(CREATED);
        } catch (Exception e) {
          failure++;
          continue;
        }
      }
      log_transactions();
      latch.countDown();
      return results;
    }
  }

  /**
   * Thread task that submits get requests for all skiers for a given day.
   */
  private static class GetThreadTask extends AbstractThreadTask {

    private static final String MYVERT = "myvert";
    private static final String TARGET = ROOT + MYVERT;
    private static final String DAY_NUM = "dayNum";
    private static final String SKIER_ID = "skierID";
    public static final int NO_CONTENT = 204;
    private int offsetBy;
    private int endAt;
    private int dayNumValue;

    /**
     * Creates a new get thread task
     *
     * @param port Port number of web server
     * @param ipAddress IP Address of web server
     * @param offsetBy Skier id number to start with in request iterations
     * @param endAt Skier id number to end with in request iterations
     */
    private GetThreadTask(Integer port, String ipAddress, int offsetBy, int endAt) {
      super(port, ipAddress);
      this.results = new ArrayList<>(SKIER_COUNT);
      this.offsetBy = offsetBy;
      this.endAt = endAt;
      this.dayNumValue = parseDayNumber(FILE_LOCATION);
    }

    public GetThreadTask(Integer port, String ipAddress, int offsetBy, int endAt, int dayNumValue) {
      super(port, ipAddress);
      this.offsetBy = offsetBy;
      this.endAt = endAt;
      this.dayNumValue = dayNumValue;
    }

    public static Integer parseDayNumber(String fileName) {
      Pattern pattern = Pattern.compile("Day([1-9]+)");
      Matcher matcher = pattern.matcher(fileName);
      if (matcher.find()) {
        return Integer.parseInt(matcher.group(1));
      }
      return null;
    }

    /**
     * Makes get requests for a number of skiers.
     *
     * @return A list of metric data.
     */
    @Override
    public Object call() throws Exception {
      List<Pair<String, Integer>> params = new ArrayList<>();
      params.add((new Pair<>(DAY_NUM, dayNumValue)));
      for (int i = offsetBy; i < endAt; i++) {
        try {
          params.add(new Pair<>(SKIER_ID, i));
          Builder builder = createRequest(TARGET, MediaType.APPLICATION_JSON, params);
          start_clock();
          response = builder.get();
          stop_clock();
          String json = response.readEntity(String.class);
          IntPair data = gson.fromJson(json, IntPair.class);
          log_response(OK);
          params.remove(1);
        } catch (Exception exc) {
          failure++;
          continue;
        }
      }
      log_transactions();
      latch.countDown();
      return results;
    }
  }
}
