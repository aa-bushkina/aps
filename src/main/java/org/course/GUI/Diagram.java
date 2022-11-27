package org.course.GUI;


import lombok.Getter;
import lombok.Setter;
import org.course.application.Controller;
import org.course.application.events.Event;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import static org.course.application.events.Type.*;

@Getter
@Setter
public class Diagram extends JPanel implements ActionListener {
  private Controller controller;
  private JPanel chartPanel;
  private int generator_low;
  private int canceled_low;
  private HashMap<Integer, Integer> devices_low;
  private HashMap<Integer, Integer> buffer_low;
  private HashMap<Integer, Double> currentVal;
  private HashMap<Integer, XYSeries> series;
  private int canceledOrderCount = 0;

  public JPanel getJPanel() {
    return chartPanel;
  }

  public Diagram(Controller controller) {
    this.controller = controller;
    series = new HashMap<>();
    devices_low = new HashMap<>();
    buffer_low = new HashMap<>();
    currentVal = new HashMap<>();
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
    axisX.setTickUnit(new NumberTickUnit(0.1, new DecimalFormat(), 0));
    NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
    axisY.setRange(0.0, 10.0);
    return result;
  }

  public void actionPerformed(final ActionEvent e) {
    if (e.getActionCommand().equals("NEXT")) {
      if (controller.getCurrentTime() == 0) {
        series.get(1).add(0, canceled_low);
        series.get(series.size()).add(controller.getCurrentTime(), generator_low);
        for (int i = 0; i < devices_low.size(); i++) {
          series.get(i + 2).add(0, devices_low.get(i));
        }
        for (int i = devices_low.size() + 2, y = 0; i < series.size(); i++, y++) {
          series.get(i).add(0, buffer_low.get(y));
        }
      }
      controller.stepMode();
      final Event event = controller.getCurrentEvent();
      final double time = controller.getCurrentTime();
      //пришел заказ
      final int generateIndex = series.size();
      final int cancelIndex = 1;
      if (event.getEventType() == Generated) {
        series.get(generateIndex).add(time, currentVal.get(generateIndex));
        upValue(generateIndex);
        series.get(generateIndex).add(time, currentVal.get(generateIndex));
        downValue(generateIndex);
        series.get(generateIndex).add(time, currentVal.get(generateIndex));
        //ПОДНЯТЬ БУФЕР TODO
        for (int i = devices_low.size() + 2, y = 0; i < series.size(); i++, y++) {
          series.get(i).add(time, buffer_low.get(y));
        }
        // int i =
        //series.get(i).add(controller.getCurrentTime(), buffer_low.get(i) + 1);
        for (int i = 0; i < devices_low.size(); i++) {
          series.get(i + 2).add(time, currentVal.get(i + 2));
        }
      } else if (event.getEventType() == Unbuffered
        && event.id != -1) {
        //ОПУСТИТЬ БУФФЕР

        //Поднять девайс
        for (int i = 0; i < devices_low.size(); i++) {
          series.get(i + 2).add(time, currentVal.get(i + 2));
        }
        upValue(event.getId() + 2);
        series.get(event.getId() + 2).add(time, currentVal.get(event.getId() + 2));
      } else if (event.getEventType() == Completed) {
        for (int i = 0; i < devices_low.size(); i++) {
          series.get(i + 2).add(time, currentVal.get(i + 2));
        }
        downValue(event.getId() + 2);
        series.get(event.getId() + 2).add(time, currentVal.get(event.getId() + 2));
        for (int i = devices_low.size() + 2, y = 0; i < series.size(); i++, y++) {
          series.get(i).add(time, buffer_low.get(y));
        }
      }
      if(canceledOrderCount!=controller.getStatistics().getCanceledOrdersCount())
      {
        series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
        upValue(cancelIndex);
        series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
        downValue(cancelIndex);
        series.get(cancelIndex).add(time, currentVal.get(cancelIndex));
      }
      series.get(series.size()).add(time, generator_low);
      series.get(1).add(time, canceled_low);
    }
  }

  private void upValue(final int lineNum) {
    currentVal.replace(lineNum, currentVal.get(lineNum) + 0.5);
  }

  private void downValue(final int lineNum) {
    currentVal.replace(lineNum, currentVal.get(lineNum) - 0.5);
  }
}
