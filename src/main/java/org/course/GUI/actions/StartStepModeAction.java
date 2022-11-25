package org.course.GUI.actions;

import org.course.GUI.frames.StepModeFrame;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.course.application.Controller.statistics;

public class StartStepModeAction extends AbstractAction {
  @NotNull
  final private JFrame chooseModeFrame;

  public StartStepModeAction(@NotNull final JFrame frame) {
    this.chooseModeFrame = frame;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    chooseModeFrame.setVisible(false);
    Controller controller = new Controller();
    StepModeFrame newFrame = new StepModeFrame(statistics, controller);
    newFrame.start();
  }
}