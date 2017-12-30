package datamap;

import org.bson.types.ObjectId;

/**
 * POJO Object for sending server metric data to queueing service
 */
public class ServerMetricData {

  public ServerMetricData() {
  }

  public enum Method {POST, GET};

  private ObjectId id;
  private Method method;
  private Long latency;
  private Boolean wasSuccessful;
  private String ipAddress;

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

  public ServerMetricData(Method method, Boolean wasSuccessful) {
    this.method = method;
    this.wasSuccessful = wasSuccessful;
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

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @Override
  public String toString() {
    return "ServerMetricData{" +
        "id=" + id +
        ", method=" + method +
        ", latency=" + latency +
        ", wasSuccessful=" + wasSuccessful +
        ", ipAddress='" + ipAddress + '\'' +
        '}';
  }
}
