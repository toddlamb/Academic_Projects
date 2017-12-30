package utilities;

import com.google.common.collect.Lists;
import datamap.MetricPair;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class LineChart extends ApplicationFrame {

  private static final int WIDTH = 640;
  private static final int HEIGHT = 480;
  private static final String PATHNAME = "LineChart.jpeg";
  private static final String LATENCY = "Avg Latency";
  public static final int MILLISEC_PER_SEC = 1000;
  private static final String TIME = "Time";
  private static final String AXIS_LABEL = "Average Latency";

  /**
   * Creates a JFreeChart LineChart jpeg
   *
   * @param applicationTitle The title of the JFreeChart AWT GUI
   * @param chartTitle The name of the jpeg chart
   * @param numberList Data used to generate the chart
   * @param pathname The location of the jpeg output
   */
  public LineChart(String applicationTitle, String chartTitle, List<MetricPair> numberList,
      String pathname) {
    super(applicationTitle);
    DefaultCategoryDataset dataset = createDataset(numberList);
    JFreeChart lineChart = ChartFactory.createLineChart(
        chartTitle,
        TIME, AXIS_LABEL,
        dataset,
        PlotOrientation.VERTICAL,
        true, false, false);
    try {
      File chartJpeg = new File(pathname);
      ChartUtilities.saveChartAsJPEG(chartJpeg, lineChart, WIDTH, HEIGHT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a dataset for chart generation given a list of metrics
   *
   * @param data List of metrics
   * @return dataset for chart
   */
  private DefaultCategoryDataset createDataset(List<MetricPair> data) {
    data.sort(new Comparator<MetricPair>() {
      @Override
      public int compare(MetricPair o1, MetricPair o2) {
        if (o1.getStartTime() == o2.getStartTime()) {
          return 0;
        } else {
          return o1.getStartTime() < o2.getStartTime() ? -1 : 1;
        }
      }
    });
    // Take one bucket of milliseconds and split into 1000 buckets of seconds
    List<List<MetricPair>> list_of_lists = Lists.partition(data, data.size() / MILLISEC_PER_SEC);
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (List<MetricPair> list : list_of_lists) {
      // Plot average latency and average start time for each bucket
      Long average_latency =
          list.stream().mapToLong(a -> (a.getEndTime() - a.getStartTime())).reduce(Long::sum)
              .getAsLong() / list.size();
      Long average_timestart =
          list.stream().mapToLong(a -> a.getStartTime()).reduce(Long::sum).getAsLong() / list
              .size();
      dataset.addValue(average_latency, LATENCY, average_timestart);
    }
    return dataset;
  }

}
