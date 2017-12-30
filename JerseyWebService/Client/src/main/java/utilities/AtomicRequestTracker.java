package utilities;

/**
 * Tracker used for keeping count of cross thread calculations/metrics.
 */
public class AtomicRequestTracker {

  private Integer requestCount;
  private Integer successCount;

  public AtomicRequestTracker() {
    this.requestCount = 0;
    this.successCount = 0;
  }


  /**
   * @param wasSuccess True if requests added were successfull, and false otherwise
   * @param requestCount The number of requests to add to the count
   */
  synchronized public void trackRequest(boolean wasSuccess, int requestCount) {
    this.requestCount += requestCount;
    if (wasSuccess) {
      successCount += requestCount;
    }
  }

  synchronized public Integer getRequestCount() {
    return requestCount;
  }

  synchronized public Integer getSuccessCount() {
    return successCount;
  }
}
