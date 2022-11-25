package org.course.GUI.frames;

import org.course.application.Controller;
import org.course.statistic.ClientStatistics;
import org.course.statistic.StatController;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class AutoModeFrame extends CustomFrame {
  StatController statistics;

  public void setStatistics(@NotNull final StatController statistics) {
    this.statistics = statistics;
  }

  public void start() {
    JFrame frame = createFrame("Auto mode");
    String[] columnNames = {"Tasks count", "Failure probability", "Total time in system",
      "Time in buffer", "Buffer dispersion", "Maintenance dispersion"};

    String[][] data = new String[Controller.statistics.getClientsCount()][6];
    int i = 0;
    for (ClientStatistics s : statistics.getClientsStats()) {
      data[i][0] = String.valueOf(s.getGeneratedTasksCount());
      data[i][1] = String.valueOf((double) s.getCanceledTasksCount() / s.getGeneratedTasksCount());
      data[i][2] = String.valueOf(s.getTotalTime());
      data[i][3] = String.valueOf(s.getTotalBufferedTime());
      data[i][4] = String.valueOf(s.getBufferDispersion());
      data[i][5] = String.valueOf(s.getTotalDispersion());
      i++;
    }
    JTable table = new JTable(data, columnNames);

    JScrollPane scroll = new JScrollPane(table);
    table.setPreferredScrollableViewportSize(new Dimension(700, 200));
    frame.getContentPane().add(scroll);
    frame.setSize(1000, 500);

    this.currentFrame = frame;
    this.revalidate();
  }
}
