package org.course.GUI.actions;

import org.course.GUI.frames.ResultsFrame;
import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GetResultsAction extends AbstractAction {
  @NotNull
  final private JFrame prevFrame;
  @NotNull
  final Controller controller;

  public GetResultsAction(@NotNull final JFrame frame,
                          @NotNull final Controller controller) {
    this.prevFrame = frame;
    this.controller = controller;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    prevFrame.setVisible(false);
    ResultsFrame modeFrame = new ResultsFrame();
    modeFrame.setStatistics(controller.getStatistics());
    modeFrame.start();
  }
}
