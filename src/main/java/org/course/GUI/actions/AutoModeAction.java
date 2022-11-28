package org.course.GUI.actions;

import org.course.GUI.Waveform;
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
  @NotNull
  private JButton buttonNext;
  @NotNull
  private Waveform waveform;

  public AutoModeAction(@NotNull final Controller controller,
                        @NotNull final NextStepAction nextStepAction,
                        @NotNull final Waveform waveform) {
    this.controller = controller;
    this.nextStepAction = nextStepAction;
    this.waveform = waveform;
    buttonNext = new JButton();
    buttonNext.setActionCommand("NEXT");
    buttonNext.addActionListener(waveform);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    while (Statistics.countOfOrders != controller.getStatistics().getCompletedOrdersCount()) {
      var i = controller.getStatistics().getCompletedOrdersCount();
      buttonNext.doClick();
      nextStepAction.actionPerformed(e);
    }
  }
}
