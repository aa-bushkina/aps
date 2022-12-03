package org.course.GUI.frames;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class CustomFrame {

  protected JFrame currentFrame;

  public static JFrame createFrame(@NotNull final String title) {
    JFrame frame = new JFrame() {
    };
    frame.setVisible(true);
    frame.setTitle(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    return frame;
  }
}
