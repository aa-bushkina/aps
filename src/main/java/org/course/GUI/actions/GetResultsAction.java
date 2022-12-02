package org.course.GUI.actions;

import org.course.GUI.frames.ResultsFrame;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GetResultsAction extends AbstractAction {
  @NotNull
  final private Controller controller;
  @NotNull
  final private JFrame prevFrame;

  public GetResultsAction(@NotNull final JFrame frame,
                          @NotNull final Controller controller) {
    this.prevFrame = frame;
    this.controller = controller;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    prevFrame.setVisible(false);
    ResultsFrame modeFrame = new ResultsFrame(controller);
    modeFrame.start();
  }
}
