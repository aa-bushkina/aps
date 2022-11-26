package org.course.GUI.frames;

import org.course.GUI.actions.FinishStepAction;
import org.course.GUI.actions.GetResultsAction;
import org.course.GUI.actions.NextStepAction;
import org.course.application.Controller;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StepModeFrame extends CustomFrame {
  final Statistics statistics;
  final Controller controller;

  public StepModeFrame(@NotNull final Statistics statistics,
                       @NotNull final Controller controller) {
    this.statistics = statistics;
    this.controller = controller;
  }

  public void start() {
    currentFrame = createFrame("Step mode");
    String[] bufferTableColumnNames = {"Index", "Insert", "Order", "Fetch"};

    String[][] bufferTableData = new String[Controller.statistics.getBufferSize()][4];
    for (int i = 0; i < controller.getBuffer().getOrders().size(); i++) {
      bufferTableData[i][0] = String.valueOf(i);
    }
    bufferTableData[0][1] = "-->";
    bufferTableData[0][2] = "";
    bufferTableData[0][3] = "<--";
    JTable bufferTable = new JTable(new DefaultTableModel(bufferTableData, bufferTableColumnNames));
    DefaultTableModel bufferTableModel = (DefaultTableModel) bufferTable.getModel();

    String[] devicesTableColumnNames = {"Index", "Order"};
    String[][] devicesTableData = new String[Controller.statistics.getDevicesCount()][2];
    for (int i = 0; i < Controller.statistics.getDevicesCount(); i++) {
      devicesTableData[i][0] = String.valueOf(i);
    }
    JTable devicesTable = new JTable(new DefaultTableModel(devicesTableData, devicesTableColumnNames));
    DefaultTableModel devicesTableModel = (DefaultTableModel) devicesTable.getModel();

    String[] resultsTableColumnNames = {"Time", "Element", "Action", "Order", "Successful requests", "Canceled requests"};
    String[][] resultsTableData = new String[0][5];
    JTable resultsTable = new JTable(new DefaultTableModel(resultsTableData, resultsTableColumnNames));
    DefaultTableModel resultsTableModel = (DefaultTableModel) resultsTable.getModel();
    resultsTable.getColumnModel().getColumn(0).setMinWidth(150);
    resultsTable.getColumnModel().getColumn(2).setMinWidth(70);

    JPanel buttonPanelNext = new JPanel(new CardLayout());
    JButton buttonNext = new JButton(
      new NextStepAction(controller, bufferTableModel, resultsTableModel, devicesTableModel));
    buttonNext.setText("Next step");
    buttonPanelNext.add(buttonNext);

    JPanel buttonPanelAuto = new JPanel(new CardLayout());
    JButton buttonAuto = new JButton(
      new FinishStepAction(controller,
        new NextStepAction(controller, bufferTableModel, resultsTableModel, devicesTableModel)));
    buttonAuto.setText("Auto mode");
    buttonPanelAuto.add(buttonAuto);

    JPanel buttonPanelResults = new JPanel(new CardLayout());
    JButton buttonResults = new JButton(new GetResultsAction(currentFrame, controller,
      new NextStepAction(controller, bufferTableModel, resultsTableModel, devicesTableModel)));
    buttonResults.setText("Results");
    buttonPanelResults.add(buttonResults);

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(buttonPanelNext);
    bottomPanel.add(buttonPanelAuto);
    bottomPanel.add(buttonPanelResults);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.add(new JLabel("Buffer"));
    leftPanel.add(bufferTable);
    leftPanel.add(Box.createVerticalStrut(50));
    leftPanel.add(new JLabel("Devices"));
    leftPanel.add(devicesTable);

    JPanel centralPanel = new JPanel();
    centralPanel.add(new JScrollPane(leftPanel));
    centralPanel.add(new JScrollPane(resultsTable));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(centralPanel);
    mainPanel.add(bottomPanel);

    currentFrame.getContentPane().setLayout(new CardLayout());
    currentFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);

    currentFrame.setSize(800, 500);
    currentFrame.revalidate();
  }
}
