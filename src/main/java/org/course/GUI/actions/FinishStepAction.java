package org.course.GUI.actions;

import org.course.application.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FinishStepAction extends AbstractAction {
  @NotNull
  private final Controller controller;
  NextStepAction nextStepAction;

  public FinishStepAction(@NotNull final Controller controller,
                          @NotNull final NextStepAction nextStepAction) {
    this.controller = controller;
    this.nextStepAction = nextStepAction;

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    while (!controller.getEvents().isEmpty()) {
      nextStepAction.actionPerformed(e);
    }
  }
}
