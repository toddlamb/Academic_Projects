package datamap;

import java.util.ArrayList;
import java.util.List;

public class MetricPair implements Comparable<MetricPair> {

  private long startTime;
  private long endTime;

  public MetricPair(long startTime, long endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public static int compare(MetricPair pair1, MetricPair pair2) {
    if (pair1.getLatency() == pair2.getLatency()) {
      return 0;
    } else {
      return pair1.getLatency() < pair2.getLatency() ? -1 : 1;
    }
  }

  public long getLatency() {
    return getEndTime() - getStartTime();
  }

  /**
   * Converts a metric pair containing start and end time to start time and latency
   *
   * @param pair An existing pair containing start and end time
   * @return a metric pair containing start and end time to start time and latency
   */
  public static MetricPair convert_to_timestamp_latency_pair(MetricPair pair) {
    return new MetricPair(pair.getStartTime(), (pair.getEndTime() - pair.getStartTime()));
  }

  /**
   * Converts a list of metric pairs to a list of lists of objects for use with google sheets.
   *
   * @param pairList An existing list of metric pairs.
   * @returna list of lists of objects for use with google sheets.
   */
  public static List<List<Object>> convert_pairs_to_lists(List<MetricPair> pairList) {
    List<List<Object>> result = new ArrayList<>();
    for (MetricPair pair : pairList) {
      List<Object> objectList = new ArrayList<>();
      objectList.add(pair.getStartTime());
      objectList.add(pair.getEndTime() - pair.getStartTime());
      result.add(objectList);
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MetricPair that = (MetricPair) o;

    if (getStartTime() != that.getStartTime()) {
      return false;
    }
    return getEndTime() == that.getEndTime();
  }

  @Override
  public int hashCode() {
    int result = (int) (getStartTime() ^ (getStartTime() >>> 32));
    result = 31 * result + (int) (getEndTime() ^ (getEndTime() >>> 32));
    return result;
  }

  @Override
  public int compareTo(MetricPair o) {
    if (equals(o)) {
      return 0;
    } else {
      return (getEndTime() - getStartTime())
          < (o.getEndTime() - o.getStartTime()) ? -1 : 1;
    }
  }

  /**
   * Gets the sum of two metric pair's latencies.
   *
   * @return the sum of two metric pair's latencies.
   */
  public static long latency_sum(MetricPair pair1, MetricPair pair2) {
    return (pair1.getEndTime() - pair1.getStartTime()) + (pair2.getEndTime() - pair2
        .getStartTime());
  }

  /**
   * Creates a new metric pair whose values are the sums of its determinant inputs.
   *
   * @return a new metric pair whose values are the sums of its determinant inputs
   */
  public static MetricPair pairSum(MetricPair pair1, MetricPair pair2) {
    return new MetricPair(pair1.getStartTime() + pair2.getStartTime(),
        pair1.getEndTime() + pair2.getEndTime());
  }
}
