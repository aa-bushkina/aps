package org.course.GUI.frames;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public abstract class CustomFrame {

  protected JFrame currentFrame;

  public void hide() {
    currentFrame.setVisible(false);
  }

  public void revalidate() {
    currentFrame.revalidate();
  }

  public static JFrame createFrame(@NotNull final String title) {
    JFrame frame = new JFrame() {
    };
    frame.setVisible(true);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    frame.setBounds(toolkit.getScreenSize().width / 4, toolkit.getScreenSize().height / 4, 400, 350);
    frame.setTitle(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }
}
