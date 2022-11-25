package org.course.GUI.frames;

import lombok.Getter;
import org.course.GUI.actions.SetDataAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class StartFrame extends CustomFrame {
  private final ArrayList<JTextField> startDataFields = new ArrayList<>();

  public void start() {
    JPanel panel = new JPanel();

    Box box = Box.createVerticalBox();

    JFrame frame = createFrame("Parameters");
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

    startDataFields.add(text1);
    startDataFields.add(text2);
    startDataFields.add(text3);
    startDataFields.add(text4);
    startDataFields.add(text5);
    startDataFields.add(text6);
    startDataFields.add(text7);

    JButton button = new JButton(new SetDataAction(this));
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
    this.currentFrame = frame;
    this.revalidate();
  }
}
