package utilities;

import datamap.MetricPair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A calculation tool for metric analysis of web client results.
 */
public class StatCalculator {

  private List<Future<List<MetricPair>>> data;
  private List<MetricPair> calculations;
  private CommandValidator arguments;
  private AtomicRequestTracker tracker;
  private Long startTime;
  private Long endTime;

  /**
   * @param data Results of all latency calculations
   * @param arguments Validated command-line arguments
   * @param tracker Response metrics
   */
  public StatCalculator(List<Future<List<MetricPair>>> data, CommandValidator arguments,
      AtomicRequestTracker tracker, Long startTime, Long endTime) {
    this.arguments = arguments;
    this.data = data;
    this.tracker = tracker;
    this.calculations = consolidateListOfLists();
    this.calculations.sort(MetricPair::compare);
    this.startTime = startTime;
    this.endTime = endTime;
  }


  /**
   * Takes list of list (one result list per thread) and consolidates into one list of values
   *
   * @return one list generated from all values in a collection of lists
   */
  private List<MetricPair> consolidateListOfLists() {
    List<MetricPair> results = new ArrayList<>(data.size());
    for (Future<List<MetricPair>> listFuture : data) {
      try {
        List<MetricPair> list = listFuture.get();
        results.addAll(list);
      } catch (InterruptedException e) {
        continue;
      } catch (ExecutionException e) {
        continue;
      } catch (NullPointerException e) {
        continue;
      }
    }
    return results;
  }

  public Long getThroughPut() {
    Long seconds = (endTime - startTime) / LineChart.MILLISEC_PER_SEC;
    return calculations.size() / seconds;
  }


  /**
   * @param percentile Desired percentile to calculate
   * @return Calculation result for nth percentile of latency calculations
   */
  public long getPercentile(int percentile) {
    double index = percentile / 100.00 * calculations.size();
    if (index % 1 == 0) {
      index = Math.ceil(index);
    }
    if ((int) index >= calculations.size()) {
      return calculations.get(calculations.size() - 1).getLatency();
    }
    return calculations.get((int) index - 1).getLatency();
  }


  /**
   * @return Mean statistic for latency calculations
   */
  public long getMean() {
    Long sum = calculations.stream().mapToLong(MetricPair::getLatency).reduce(Long::sum)
        .getAsLong();
    return sum / calculations.size();
  }

  /**
   * @return Median statistic for latency calculations
   */
  public long getMedian() {
    if (calculations.size() % 2 == 0) {
      double middle = calculations.size() / 2;
      MetricPair left_median = calculations.get((int) Math.ceil(middle));
      MetricPair right_median = calculations.get((int) Math.floor(middle));
      return (left_median.getLatency() + right_median.getLatency()) / 2;
    } else {
      return calculations.get(calculations.size() / 2).getLatency();
    }
  }

  public List<MetricPair> getCalculations() {
    return calculations;
  }
}
