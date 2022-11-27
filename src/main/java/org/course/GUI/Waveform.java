package org.course.GUI;

import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Waveform {
  public JPanel createChartPanel(@NotNull final Controller controller) {
    String chartTitle = "Waveform";
    String xAxisLabel = "Time";
    String yAxisLabel = "Element";

    XYDataset dataset = createDataset(controller);

    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(400,400));

    return panel;
  }

  private XYDataset createDataset(@NotNull final Controller controller) {
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries series1 = new XYSeries("Client");
    XYSeries series2 = new XYSeries("Buffer");
    XYSeries series3 = new XYSeries("Distributor");

    /*double client_y = 6.0;
    double buffer_y = 4.0;
    double device_y = 2.0;
    for (double i = 0.0; i < statistics.getTotalTime(); i += controller.getCurrentTime()) {
      series1.add(i, client_y);
      series2.add(i, buffer_y);
      series3.add(i, device_y);
    }*/

    dataset.addSeries(series1);
    dataset.addSeries(series2);
    dataset.addSeries(series3);

    return dataset;
  }
}

