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
    String[] columnNames = {"Tasks count", "Failure probability", "Total time in system",
      "Time in buffer", "Buffer dispersion", "Maintenance dispersion"};

    String[][] data = new String[Controller.statistics.getClientsCount()][6];
    int i = 0;
    for (ClientStatistics s : controller.getStatistics().getClientsStats()) {
      data[i][0] = String.valueOf(s.getGeneratedTasksCount());
      data[i][1] = String.valueOf((double) s.getCanceledTasksCount() / s.getGeneratedTasksCount());
      data[i][2] = String.valueOf(s.getTotalTime());
      data[i][3] = String.valueOf(s.getTotalBufferedTime());
      data[i][4] = String.valueOf(s.getBufferDispersion());
      data[i][5] = String.valueOf(s.getTotalDispersion());
      i++;
    }
    JTable table = new JTable(data, columnNames);

/*    JPanel buttonPanelWaveform = new JPanel(new CardLayout());
    JButton buttonWaveform = new JButton(new GetWaveformAction(currentFrame, controller));
    buttonWaveform.setText("Waveform");
    buttonPanelWaveform.add(buttonWaveform);

    currentFrame.getContentPane().add(buttonPanelWaveform, BorderLayout.SOUTH);*/

    JScrollPane scroll = new JScrollPane(table);
    table.setPreferredScrollableViewportSize(new Dimension(500, 900));
    currentFrame.getContentPane().add(scroll);
    currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    currentFrame.setUndecorated(true);
    currentFrame.revalidate();
  }
}
