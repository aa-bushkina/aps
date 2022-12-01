package org.course.GUI.frames;

import org.course.application.Controller;
import org.course.statistic.ClientStatistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class ResultsFrame extends CustomFrame {
  @NotNull
  final Controller controller;

  public ResultsFrame(@NotNull final Controller controller) {
    this.controller = controller;
  }

  public void start() {
    currentFrame = createFrame("Auto mode");
    String[] columnNames = {"Tasks count", "Failure probability", "Time in system",
      "Time in buffer", "Time in device", "Buffer dispersion", "Device dispersion", "Efficient"};

    String[][] data = new String[Controller.statistics.getClientsCount()][8];
    int i = 0;
    for (ClientStatistics clientStat : controller.getStatistics().getClientsStats()) {
      data[i][0] = String.valueOf(clientStat.getGeneratedTasksCount());
      data[i][1] = String.valueOf((double) clientStat.getCanceledTasksCount() / clientStat.getGeneratedTasksCount());
      data[i][2] = String.valueOf(clientStat.getTotalTime() / clientStat.getGeneratedTasksCount());
      data[i][3] = String.valueOf(clientStat.getTotalBufferedTime() / clientStat.getGeneratedTasksCount());
      data[i][4] = String.valueOf(clientStat.getTotalDeviceTime() / clientStat.getGeneratedTasksCount());
      data[i][5] = String.valueOf(clientStat.getBufferDispersion());
      data[i][6] = String.valueOf(clientStat.getDeviceDispersion());
      if (i < Controller.statistics.getDevicesCount())
        data[i][7] = String.valueOf(
          Controller.statistics.getDevicesTime().get(i) / controller.getCurrentTime());
      i++;
    }
    JTable table = new JTable(data, columnNames);

    JScrollPane scroll = new JScrollPane(table);
    table.setPreferredScrollableViewportSize(new Dimension(500, 900));
    currentFrame.getContentPane().add(scroll);
    currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    currentFrame.revalidate();
  }
}
