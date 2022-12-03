package org.course.GUI.frames;

import org.course.GUI.Waveform;
import org.course.GUI.actions.AutoModeAction;
import org.course.GUI.actions.GetResultsAction;
import org.course.GUI.actions.NextStepAction;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StepModeFrame extends CustomFrame {
  @NotNull
  final Controller controller;
  @NotNull
  final Waveform waveform;

  public StepModeFrame(@NotNull final Controller controller,
                       @NotNull final Waveform waveform) {
    this.controller = controller;
    this.waveform = waveform;
  }

  public void start() {
    currentFrame = createFrame("Step mode");

    //buffer table
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
    bufferTable.setMaximumSize(new Dimension(200, 200));

    //device table
    String[] devicesTableColumnNames = {"Index", "Order"};
    String[][] devicesTableData = new String[Controller.statistics.getDevicesCount()][2];
    for (int i = 0; i < Controller.statistics.getDevicesCount(); i++) {
      devicesTableData[i][0] = String.valueOf(i);
    }
    JTable devicesTable = new JTable(new DefaultTableModel(devicesTableData, devicesTableColumnNames));
    DefaultTableModel devicesTableModel = (DefaultTableModel) devicesTable.getModel();
    devicesTable.setMaximumSize(new Dimension(200, 200));

    //results table
    String[] resultsTableColumnNames = {"Time", "Element", "Action", "Order", "Successful requests", "Canceled requests"};
    String[][] resultsTableData = new String[0][5];
    JTable resultsTable = new JTable(new DefaultTableModel(resultsTableData, resultsTableColumnNames));
    DefaultTableModel resultsTableModel = (DefaultTableModel) resultsTable.getModel();
    resultsTable.getColumnModel().getColumn(0).setMinWidth(150);
    resultsTable.getColumnModel().getColumn(2).setMinWidth(70);

    //buttons
    JPanel buttonPanelNext = new JPanel(new CardLayout());
    JButton buttonNext = new JButton(
      new NextStepAction(controller, bufferTableModel, resultsTableModel, devicesTableModel, waveform));
    buttonNext.setText("Next step");
    buttonPanelNext.add(buttonNext);

    JPanel buttonPanelAuto = new JPanel(new CardLayout());
    JButton buttonAuto = new JButton(
      new AutoModeAction(controller,
        new NextStepAction(controller, bufferTableModel, resultsTableModel, devicesTableModel, waveform)));
    buttonAuto.setText("Auto mode");
    buttonPanelAuto.add(buttonAuto);

    JPanel buttonPanelResults = new JPanel(new CardLayout());
    JButton buttonResults = new JButton(new GetResultsAction(currentFrame, controller));
    buttonResults.setText("Results");
    buttonPanelResults.add(buttonResults);

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(buttonPanelNext);
    bottomPanel.add(buttonPanelAuto);
    bottomPanel.add(buttonPanelResults);

    //diagram panel
    JPanel diagramPanel = new JPanel();
    diagramPanel.add(new JScrollPane(waveform.getJPanel()));

    //buffer panel
    JPanel bufferPanel = new JPanel();
    bufferPanel.setLayout(new BoxLayout(bufferPanel, BoxLayout.Y_AXIS));
    bufferPanel.add(new JLabel("Buffer"));
    bufferPanel.add(new JScrollPane(bufferTable));

    //devices panel
    JPanel devicesPanel = new JPanel();
    devicesPanel.setLayout(new BoxLayout(devicesPanel, BoxLayout.Y_AXIS));
    devicesPanel.add(new JLabel("Devices"));
    devicesPanel.add(new JScrollPane(devicesTable));

    JPanel leftTablesPanel = new JPanel();
    leftTablesPanel.setLayout(new BoxLayout(leftTablesPanel, BoxLayout.X_AXIS));
    leftTablesPanel.add(bufferPanel);
    leftTablesPanel.add(devicesPanel);

    JPanel leftBigPanel = new JPanel();
    leftBigPanel.setLayout(new BoxLayout(leftBigPanel, BoxLayout.Y_AXIS));
    leftBigPanel.add(new JScrollPane(diagramPanel));
    leftBigPanel.add(leftTablesPanel);

    JPanel centralPanel = new JPanel();
    centralPanel.setLayout(new GridLayout());
    centralPanel.add(leftBigPanel);
    centralPanel.add(new JScrollPane(resultsTable));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(centralPanel);
    mainPanel.add(bottomPanel);

    currentFrame.getContentPane().setLayout(new CardLayout());
    currentFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);

    currentFrame.revalidate();
  }
}
