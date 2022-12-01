package org.course.GUI.frames;

import lombok.Getter;
import org.course.GUI.actions.StartStepMode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class StartFrame extends CustomFrame {
  private final ArrayList<JTextField> startDataFields = new ArrayList<>();

  public void start() {
    JPanel panel = new JPanel();
    Box box = Box.createVerticalBox();

    currentFrame = createFrame("Properties");
    currentFrame.add(panel);

    JLabel label1 = new JLabel("Devices");
    label1.setPreferredSize(new Dimension(200, 30));

    JLabel label2 = new JLabel("Clients");
    label2.setPreferredSize(new Dimension(200, 30));

    JLabel label3 = new JLabel("Tasks");
    label3.setPreferredSize(new Dimension(200, 30));

    JLabel label4 = new JLabel("Buffer size");
    label4.setPreferredSize(new Dimension(200, 30));

    JLabel label5 = new JLabel("Min application processing time");
    label5.setPreferredSize(new Dimension(200, 30));

    JLabel label6 = new JLabel("Max application processing time");
    label6.setPreferredSize(new Dimension(200, 30));

    JLabel label7 = new JLabel("Lambda for generation");
    label7.setPreferredSize(new Dimension(200, 30));

    final int boxesCount = 7;
    ArrayList<Box> boxList = new ArrayList<>(boxesCount);
    for (int i = 0; i < boxesCount; i++) {
      boxList.add(Box.createHorizontalBox());
    }

    boxList.get(0).add(label1);
    JTextField text1 = new JTextField("3", 10);
    boxList.get(0).add(text1);

    boxList.get(1).add(label2);
    JTextField text2 = new JTextField("20", 10);
    boxList.get(1).add(text2);

    boxList.get(2).add(label3);
    JTextField text3 = new JTextField("500", 10);
    boxList.get(2).add(text3);

    boxList.get(3).add(label4);
    JTextField text4 = new JTextField("4", 10);
    boxList.get(3).add(text4);

    boxList.get(4).add(label5);
    JTextField text5 = new JTextField("0.1", 10);
    boxList.get(4).add(text5);

    boxList.get(5).add(label6);
    JTextField text6 = new JTextField("0.2", 10);
    boxList.get(5).add(text6);

    boxList.get(6).add(label7);
    JTextField text7 = new JTextField("0.3", 10);
    boxList.get(6).add(text7);

    startDataFields.add(text1);
    startDataFields.add(text2);
    startDataFields.add(text3);
    startDataFields.add(text4);
    startDataFields.add(text5);
    startDataFields.add(text6);
    startDataFields.add(text7);

    final JLabel title = new JLabel("Enter properties:");

    final JButton button = new JButton(new StartStepMode(currentFrame, startDataFields));
    button.setText("ok");

    box.add(Box.createVerticalStrut(100));
    box.add(title);
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(0));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(1));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(2));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(3));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(4));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(5));
    box.add(Box.createVerticalStrut(10));
    box.add(boxList.get(6));
    box.add(Box.createVerticalStrut(25));
    box.add(button);
    panel.add(box);
    currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    currentFrame.revalidate();
  }
}
