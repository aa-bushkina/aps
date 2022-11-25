package org.course.GUI.frames;


import org.course.GUI.actions.StartAutoMode;
import org.course.GUI.actions.StartStepModeAction;

import javax.swing.*;
import java.awt.*;


public class ChooseModeFrame extends CustomFrame {
  public void start() {
    currentFrame = createFrame("Mode");
    JPanel panel = new JPanel();
    Box box = Box.createVerticalBox();
    JButton autoModeButton = new JButton(new StartAutoMode(currentFrame));
    autoModeButton.setText("Table");

    JButton stepModeButton = new JButton(new StartStepModeAction(currentFrame));
    stepModeButton.setText("Step Mode");

    JButton waveformButton = new JButton(/*new StartStepModeAction()*/);
    waveformButton.setText("Waveform");

    JLabel label = new JLabel("Choose action");
    label.setPreferredSize(new Dimension(panel.getWidth(), 20));
    label.setHorizontalAlignment(SwingConstants.CENTER);

    Box horizontalBoxBut0 = Box.createHorizontalBox();
    horizontalBoxBut0.add(Box.createHorizontalGlue());
    horizontalBoxBut0.add(autoModeButton);
    horizontalBoxBut0.add(Box.createHorizontalGlue());
    Box horizontalBoxBut1 = Box.createHorizontalBox();
    horizontalBoxBut1.add(Box.createHorizontalGlue());
    horizontalBoxBut1.add(stepModeButton);
    horizontalBoxBut1.add(Box.createHorizontalGlue());
    Box horizontalBoxBut2 = Box.createHorizontalBox();
    horizontalBoxBut2.add(Box.createHorizontalGlue());
    horizontalBoxBut2.add(waveformButton);
    horizontalBoxBut2.add(Box.createHorizontalGlue());

    box.add(Box.createVerticalStrut(70));
    box.add(horizontalBoxBut0);
    box.add(Box.createVerticalStrut(40));
    box.add(horizontalBoxBut1);
    box.add(Box.createVerticalStrut(40));
    box.add(horizontalBoxBut2);

    panel.add(label);
    panel.add(box);

    currentFrame.add(panel);
    currentFrame.revalidate();
  }
}
