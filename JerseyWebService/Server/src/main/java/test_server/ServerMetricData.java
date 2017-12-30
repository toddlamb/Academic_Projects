package test_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * POJO Object for sending server metric data to queueing service
 */
public class ServerMetricData {


  public enum Method {POST, GET}

  private Method method;
  private Long latency;
  private Boolean wasSuccessful;
  private String ipAddress;
  private static String publicIpAddress;

  public static final String CHECK_IP_URL = "http://checkip.amazonaws.com";

  /**
   * Gets ip address for use by all instances.
   */
  static {
    URL url = null;
    String result = null;
    try {
      url = new URL(CHECK_IP_URL);
      BufferedReader in = null;
      in = new BufferedReader(new InputStreamReader(url.openStream()));
      result = in.readLine();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      publicIpAddress = result;
    }
  }

  /**
   *
   * @param method Http request method from the client
   * @param latency Time for database call associated with request
   * @param wasSuccessful Indicates if database call was successful
   * @param ipAddress Ip address of server that handled request.
   */
  public ServerMetricData(Method method, Long latency, Boolean wasSuccessful, String ipAddress) {
    this.method = method;
    this.latency = latency;
    this.wasSuccessful = wasSuccessful;
    this.ipAddress = ipAddress;
  }

  public ServerMetricData(Method method, Boolean wasSuccessful, String ipAddress) {
    this.method = method;
    this.wasSuccessful = wasSuccessful;
    this.ipAddress = ipAddress;
  }

  public ServerMetricData(Method method, Long latency, Boolean wasSuccessful) {
    this.method = method;
    this.latency = latency;
    this.wasSuccessful = wasSuccessful;
    this.ipAddress = getPublicIpAddress();
  }

  public ServerMetricData(Method method, Boolean wasSuccessful) {
    this.method = method;
    this.wasSuccessful = wasSuccessful;
    this.ipAddress = publicIpAddress;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Long getLatency() {
    return latency;
  }

  public void setLatency(Long latency) {
    this.latency = latency;
  }

  public Boolean getWasSuccessful() {
    return wasSuccessful;
  }

  public void setWasSuccessful(Boolean wasSuccessful) {
    this.wasSuccessful = wasSuccessful;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public static String getPublicIpAddress() {
    return publicIpAddress;
  }
}
