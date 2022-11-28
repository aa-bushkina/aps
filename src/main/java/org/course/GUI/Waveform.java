package org.course.GUI;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Controller;
import org.course.application.Order;
import org.course.application.events.Event;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static org.course.application.events.Type.*;

@Getter
@Setter
public class Waveform extends JPanel implements ActionListener {
  private Controller controller;
  private JPanel chartPanel;
  private int generator_low;
  private int canceled_low;
  private HashMap<Integer, Integer> devices_low;
  private HashMap<Integer, Integer> buffer_low;
  private HashMap<Integer, Double> currentVal;
  private HashMap<Integer, XYSeries> series;
  private int canceledOrderCount = 0;
  final ArrayList<Order> bufferList;
  private boolean ifFirstIteration = true;

  public JPanel getJPanel() {
    return chartPanel;
  }

  public Waveform(@NotNull final Controller controller) {
    this.controller = controller;
    bufferList = new ArrayList<>(controller.getBuffer().getSize());
    for (int i = 0; i < controller.getBuffer().getCapacity(); i++) {
      bufferList.add(null);
    }
    series = new HashMap<>(1048576);
    devices_low = new HashMap<>(30);
    buffer_low = new HashMap<>(30);
    currentVal = new HashMap<>(30);
    canceled_low = 1;
    currentVal.put(1, (double) canceled_low);
    series.put(1, new XYSeries("Canceled"));
    for (int i = 0; i < controller.getStatistics().getDevicesCount(); i++) {
      devices_low.put(i, i + 2);
      series.put(i + 2, new XYSeries("Device" + i));
      currentVal.put(i + 2, i + 2.0);
    }

    for (int i = 0; i < controller.getStatistics().getBufferSize(); i++) {
      buffer_low.put(i, devices_low.size() + 2 + i);
      series.put(devices_low.size() + 2 + i, new XYSeries("Buffer" + i));
      currentVal.put(devices_low.size() + 2 + i, (double) devices_low.size() + 2 + i);
    }

    generator_low = devices_low.size() + buffer_low.size() + 2;
    series.put(series.size() + 1, new XYSeries("Generator"));
    currentVal.put(series.size(), (double) series.size());

    XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
    for (int i = 0; i < series.size(); i++) {
      xySeriesCollection.addSeries(series.get(i + 1));
    }

    JFreeChart chart = ChartFactory.createXYLineChart(
      "Waveform", "Time", "Element", xySeriesCollection);

    chart.setBackgroundPaint(Color.white);

    final XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(new Color(49, 49, 49));

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    for (int i = 0; i < series.size(); i++) {
      renderer.setSeriesShapesVisible(i, false);
      renderer.setSeriesStroke(i, new BasicStroke(2.0f));
    }
    renderer.setSeriesPaint(0, Color.RED);
    for (int i = 1; i < devices_low.size() + 1; i++) {
      renderer.setSeriesPaint(i, Color.orange);
    }
    for (int i = devices_low.size() + 1; i < series.size() - 1; i++) {
      renderer.setSeriesPaint(i, Color.cyan);
    }
    renderer.setSeriesPaint(series.size() - 1, Color.MAGENTA);

    plot.setRenderer(renderer);

    final ChartPanel chartPanel = new ChartPanel(chart);
    JPanel content = new JPanel(new BorderLayout());
    content.add(chartPanel);
    chartPanel.setPreferredSize(new Dimension(2000, 400));
    this.chartPanel = content;
  }

  public void actionPerformed(final ActionEvent e) {
    if (ifFirstIteration) {
      ifFirstIteration = false;
      series.get(1).add(0, canceled_low);
      series.get(series.size()).add(controller.getCurrentTime(), generator_low);
      for (int i = 0; i < devices_low.size(); i++) {
        series.get(i + 2).add(0, devices_low.get(i));
      }
      for (int i = devices_low.size() + 2, y = 0; i < series.size(); i++, y++) {
        series.get(i).add(0, buffer_low.get(y));
      }
    }
    final Event event = controller.getCurrentEvent();
    final double time = controller.getCurrentTime();
    final int generateIndex = series.size();
    final int cancelIndex = 1;
    if (event.getEventType() == Generated) {
      series.get(generateIndex).add(time, currentVal.get(generateIndex));
      upValue(generateIndex);
      series.get(generateIndex).add(time, currentVal.get(generateIndex));
      downValue(generateIndex);
      series.get(generateIndex).add(time, currentVal.get(generateIndex));
      //up buffer
      for (int i = devices_low.size() + 2; i < series.size(); i++) {
        series.get(i).add(time, currentVal.get(i));
      }
      final int bufferIndex = findDifferenceInLists(controller.getBuffer().getOrders());
      if (bufferIndex != -1) {
        upValue(devices_low.size() + 2 + bufferIndex);
      }
      series.get(devices_low.size() + 2 + bufferIndex)
        .add(time, currentVal.get(devices_low.size() + 2 + bufferIndex));
      for (int i = 0; i < devices_low.size(); i++) {
        series.get(i + 2).add(time, currentVal.get(i + 2));
      }
    } else if (event.getEventType() == Unbuffered
      && event.id != -1) {
      //down buffer
      for (int i = devices_low.size() + 2; i < series.size(); i++) {
        series.get(i).add(time, currentVal.get(i));
      }
      final int bufferIndex = findDifferenceInLists(controller.getBuffer().getOrders());
      if (bufferIndex != -1) {
        downValue(devices_low.size() + 2 + bufferIndex);
      }
      series.get(devices_low.size() + 2 + bufferIndex)
        .add(time, currentVal.get(devices_low.size() + 2 + bufferIndex));
      //up device
      for (int i = 0; i < devices_low.size(); i++) {
        series.get(i + 2).add(time, currentVal.get(i + 2));
      }
      upValue(event.getId() + 2);
      series.get(event.getId() + 2).add(time, currentVal.get(event.getId() + 2));
    } else if (event.getEventType() == Completed) {
      //down device
      for (int i = 0; i < devices_low.size(); i++) {
        series.get(i + 2).add(time, currentVal.get(i + 2));
      }
      downValue(event.getId() + 2);
      series.get(event.getId() + 2).add(time, currentVal.get(event.getId() + 2));
      for (int i = devices_low.size() + 2; i < series.size(); i++) {
        series.get(i).add(time, currentVal.get(i));
      }
    }
    if (canceledOrderCount != controller.getStatistics().getCanceledOrdersCount()) {
      series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
      upValue(cancelIndex);
      series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
      downValue(cancelIndex);
      series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
      canceledOrderCount = controller.getStatistics().getCanceledOrdersCount();
    }
    series.get(series.size()).add(time, generator_low);
    series.get(1).add(time, canceled_low);
  }

  private void upValue(final int lineNum) {
    currentVal.replace(lineNum, currentVal.get(lineNum) + 0.5);
  }

  private void downValue(final int lineNum) {
    currentVal.replace(lineNum, currentVal.get(lineNum) - 0.5);
  }

  private int findDifferenceInLists(final ArrayList<Order> list) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) != bufferList.get(i)) {
        bufferList.set(i, list.get(i));
        return i;
      }
    }
    return -1;
  }
}
