package org.course.GUI.frames;


import org.course.GUI.actions.StartAutoMode;

import javax.swing.*;
import java.awt.*;


public class ChooseModeFrame extends CustomFrame {
  public void start() {
    JFrame frame = createFrame("Mode");
    JPanel panel = new JPanel();

    JButton autoModeButton = new JButton(new StartAutoMode(this));
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

    this.currentFrame = frame;
    this.revalidate();
  }
}
