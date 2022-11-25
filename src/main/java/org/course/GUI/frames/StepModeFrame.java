package org.course.GUI.frames;

import org.course.GUI.actions.NextStepAction;
import org.course.application.Controller;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StepModeFrame extends CustomFrame {
  final Statistics statistics;
  final Controller controller;

  public StepModeFrame(@NotNull final Statistics statistics, @NotNull final Controller controller) {
    this.statistics = statistics;
    this.controller = controller;
  }

  public void start() {
    currentFrame = createFrame("Step mode");
    String[] bufferTableColumnNames = {"", "Insert", "Order", "Fetch"};

    String[][] bufferTableData = new String[Controller.statistics.getBufferSize()][4];
    for (int i = 0; i < controller.getBuffer().getOrders().size(); i++) {
      bufferTableData[i][0] = String.valueOf(i);
    }
    bufferTableData[0][1] = "-->";
    bufferTableData[0][2] = "";
    bufferTableData[0][3] = "<--";
    JTable bufferTable = new JTable(new DefaultTableModel(bufferTableData, bufferTableColumnNames));
    DefaultTableModel bufferTableModel = (DefaultTableModel) bufferTable.getModel();


    String[] resultsTableColumnNames = {"Time", "Element", "Action", "Successful requests", "Canceled requests"};
    String[][] resultsTableData = new String[0][5];
    JTable resultsTable = new JTable(new DefaultTableModel(resultsTableData, resultsTableColumnNames));
    DefaultTableModel resultsTableModel = (DefaultTableModel) resultsTable.getModel();

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton button = new JButton(new NextStepAction(controller, bufferTableModel, resultsTableModel));
    button.setText("next step");
    buttonPanel.add(button);

    currentFrame.getContentPane().setLayout(new BorderLayout());
    currentFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    currentFrame.getContentPane().add(new JScrollPane(bufferTable), BorderLayout.CENTER);
    currentFrame.getContentPane().add(new JScrollPane(resultsTable), BorderLayout.EAST);

    currentFrame.setSize(1000, 500);
    currentFrame.revalidate();
  }
}
