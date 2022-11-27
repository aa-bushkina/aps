package org.course.GUI;


import lombok.Getter;
import lombok.Setter;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

@Getter
@Setter
public class Diagram extends JPanel implements ActionListener {
  private XYSeries series;
  private double lastValue = 0.0;
  private Controller controller;
  private JPanel chartPanel;

  public JPanel getJPanel() {
    return chartPanel;
  }

  public Diagram(Controller controller) {
    this.controller = controller;
    this.series = new XYSeries("Time");
    final XYDataset dataset = new XYSeriesCollection(series);
    final JFreeChart chart = createChart(dataset);

    final ChartPanel chartPanel = new ChartPanel(chart);
    JPanel content = new JPanel(new BorderLayout());
    content.add(chartPanel);
    chartPanel.setPreferredSize(new Dimension(2000, 400));
    this.chartPanel = content;
  }

  private JFreeChart createChart(@NotNull final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createXYLineChart(
      "Waveform",
      "Time",
      "Element",
      dataset
    );
    final XYPlot plot = result.getXYPlot();
    NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
    axisX.setRange(0.00, 160.00);
    axisX.setStandardTickUnits(NumberAxis.createStandardTickUnits());
    axisX.setTickUnit(new NumberTickUnit(0.01, new DecimalFormat(), 0));
    NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
    axisY.setRange(0.0, 10.0);
    return result;
  }

  public void actionPerformed(final ActionEvent e) {
    if (e.getActionCommand().equals("NEXT")) {
      this.lastValue = 5;
      this.series.add(controller.getCurrentTime(), lastValue);
    }
  }
}
