package org.course.GUI.actions;

import org.course.application.Controller;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AutoModeAction extends AbstractAction {
  @NotNull
  private final Controller controller;
  @NotNull
  private final NextStepAction nextStepAction;

  public AutoModeAction(@NotNull final Controller controller,
                        @NotNull final NextStepAction nextStepAction) {
    this.controller = controller;
    this.nextStepAction = nextStepAction;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    while (Statistics.workTime > controller.getCurrentTime() && !controller.getEvents().isEmpty()) {
      nextStepAction.actionPerformed(e);
    }
  }
}
