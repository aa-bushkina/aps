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

    final int elementsCount = 7;

    ArrayList<JLabel> labelsList = new ArrayList<>(elementsCount);
    labelsList.add(0, new JLabel("Devices"));
    labelsList.add(1, new JLabel("Clients"));
    labelsList.add(2, new JLabel("Tasks"));
    labelsList.add(3, new JLabel("Buffer size"));
    labelsList.add(4, new JLabel("Min application processing time"));
    labelsList.add(5, new JLabel("Max application processing time"));
    labelsList.add(6, new JLabel("Lambda for generation"));

    ArrayList<JTextField> textList = new ArrayList<>(elementsCount);
    textList.add(new JTextField("3", 10));
    textList.add(new JTextField("20", 10));
    textList.add(new JTextField("500", 10));
    textList.add(new JTextField("4", 10));
    textList.add(new JTextField("0.1", 10));
    textList.add(new JTextField("0.2", 10));
    textList.add(new JTextField("0.3", 10));

    ArrayList<Box> boxList = new ArrayList<>(elementsCount);
    for (int i = 0; i < elementsCount; i++) {
      labelsList.get(i).setPreferredSize(new Dimension(200, 30));
      boxList.add(Box.createHorizontalBox());
      boxList.get(i).add(labelsList.get(i));
      boxList.get(i).add(textList.get(i));
      startDataFields.add(textList.get(i));
    }

    final JLabel title = new JLabel("Enter properties:");

    final JButton button = new JButton(new StartStepMode(currentFrame, startDataFields));
    button.setText("ok");

    box.add(Box.createVerticalStrut(100));
    box.add(title);
    for (Box value : boxList) {
      box.add(Box.createVerticalStrut(10));
      box.add(value);
    }
    box.add(Box.createVerticalStrut(25));
    box.add(button);
    panel.add(box);
    currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    currentFrame.revalidate();
  }
}
