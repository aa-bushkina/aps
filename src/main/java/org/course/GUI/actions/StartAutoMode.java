package org.course.GUI.actions;

import org.course.GUI.frames.AutoModeFrame;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StartAutoMode extends AbstractAction {
  @NotNull
  final private JFrame chooseModeFrame;

  public StartAutoMode(@NotNull final JFrame frame) {
    this.chooseModeFrame = frame;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Controller controller = new Controller();
    controller.autoMode();
    chooseModeFrame.setVisible(false);
    AutoModeFrame modeFrame = new AutoModeFrame();
    modeFrame.setStatistics(controller.getStatistics());
    modeFrame.start();
  }
}
