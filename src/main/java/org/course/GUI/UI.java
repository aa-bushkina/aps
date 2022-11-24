package org.course.GUI;

import org.course.application.Controller;
import org.course.statistic.ClientStat;
import org.course.statistic.StatController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class UI {

  static JFrame startFrame;
  static JFrame modeFrame;
  static JFrame autoModeFrame;

  private final ArrayList<JTextField> startDataFields = new ArrayList<>();
  private Controller controller;
  StatController stat = StatController.getInstance();

  public void execute() throws InterruptedException {
    startFrame = getStartFrame();
    while (startFrame.isVisible()) {
      Thread.sleep(100);
    }
    modeFrame = getModeFrame();
  }

  class SetDataAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      StatController.countOfRestaurantDevices = Integer.parseInt(startDataFields.get(0).getText());
      StatController.countOfClients = Integer.parseInt(startDataFields.get(1).getText());
      StatController.countOfRequiredOrders = Integer.parseInt(startDataFields.get(2).getText());
      StatController.sizeOfBuffer = Integer.parseInt(startDataFields.get(3).getText());
      StatController.minimum = Double.parseDouble(startDataFields.get(4).getText());
      StatController.maximum = Double.parseDouble(startDataFields.get(5).getText());
      StatController.lambda = Double.parseDouble(startDataFields.get(6).getText());

      controller = new Controller();

      stat = StatController.getInstance();
      startFrame.setVisible(false);
      startFrame.revalidate();
    }
  }

  class StartAutoModeAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      modeFrame.setVisible(false);

      controller.autoMode();
      stat = controller.getStatistics();

      autoModeFrame = getAutoModeFrame();
    }
  }

  private JFrame getFrame(String title) {
    JFrame frame = new JFrame() {
    };
    frame.setVisible(true);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    frame.setBounds(toolkit.getScreenSize().width / 4, toolkit.getScreenSize().height / 4, 400, 350);
    frame.setTitle(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }

  private JFrame getStartFrame() {
    JPanel panel = new JPanel();

    Box box = Box.createVerticalBox();

    JFrame frame = getFrame("Parameters");
    frame.add(panel);

    JLabel label1 = new JLabel("Devices count");
    label1.setPreferredSize(new Dimension(100, 40));

    JLabel label2 = new JLabel("Clients count");
    label2.setPreferredSize(new Dimension(100, 40));

    JLabel label3 = new JLabel("Tasks count");
    label3.setPreferredSize(new Dimension(100, 40));

    JLabel label4 = new JLabel("Buffer size");
    label4.setPreferredSize(new Dimension(100, 40));

    JLabel label5 = new JLabel("Min");
    label5.setPreferredSize(new Dimension(100, 40));

    JLabel label6 = new JLabel("Max");
    label6.setPreferredSize(new Dimension(100, 40));

    JLabel label7 = new JLabel("Lambda");
    label7.setPreferredSize(new Dimension(100, 40));

    Box string1 = Box.createHorizontalBox();
    string1.add(label1);
    JTextField text1 = new JTextField("3", 20);
    string1.add(text1);

    Box string2 = Box.createHorizontalBox();
    string2.add(label2);
    JTextField text2 = new JTextField("50", 20);
    string2.add(text2);

    Box string3 = Box.createHorizontalBox();
    string3.add(label3);
    JTextField text3 = new JTextField("2000", 20);
    string3.add(text3);

    Box string4 = Box.createHorizontalBox();
    string4.add(label4);
    JTextField text4 = new JTextField("4", 20);
    string4.add(text4);

    Box string5 = Box.createHorizontalBox();
    string5.add(label5);
    JTextField text5 = new JTextField("0.1", 20);
    string5.add(text5);

    Box string6 = Box.createHorizontalBox();
    string6.add(label6);
    JTextField text6 = new JTextField("0.2", 20);
    string6.add(text6);

    Box string7 = Box.createHorizontalBox();
    string7.add(label7);
    JTextField text7 = new JTextField("0.3", 20);
    string7.add(text7);

    JButton button = new JButton(new SetDataAction());
    button.setText("start");
    box.add(string1);
    box.add(string3);
    box.add(string4);
    box.add(string5);
    box.add(string6);
    box.add(string7);
    box.add(Box.createVerticalStrut(25));
    box.add(button);
    panel.add(box);

    frame.revalidate();

    startDataFields.add(text1);
    startDataFields.add(text2);
    startDataFields.add(text3);
    startDataFields.add(text4);
    startDataFields.add(text5);
    startDataFields.add(text6);
    startDataFields.add(text7);

    return frame;
  }

  private JFrame getModeFrame() {

    JFrame frame = getFrame("Mode");
    JPanel panel = new JPanel();

    JButton autoModeButton = new JButton(new StartAutoModeAction());
    autoModeButton.setText("Table");

    JButton stepModeButton = new JButton(/*new StartStepModeAction()*/);
    stepModeButton.setText("Waveform");

    JLabel label = new JLabel("Choose action");
    label.setPreferredSize(new Dimension(panel.getWidth(), 20));
    label.setHorizontalAlignment(SwingConstants.CENTER);

    panel.add(label);
    panel.add(autoModeButton);
    panel.add(stepModeButton);

    frame.add(panel);
    frame.revalidate();

    return frame;
  }

  private JFrame getAutoModeFrame() {
    JFrame frame = getFrame("Auto mode");
    String[] columnNames = {"Tasks count", "Failure probability", "Total time in system",
      "Time in buffer", "Buffer dispersion", "Maintenance dispersion"};

    String[][] data = new String[Controller.statistics.getClientsCount()][6];
    int i = 0;
    for (ClientStat s : stat.getClientsStats()) {
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
    frame.revalidate();

    return frame;
  }
}
