package org.course.GUI.actions;

import org.course.GUI.Waveform;
import org.course.GUI.frames.StepModeFrame;
import org.course.application.Controller;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class StartStepMode extends AbstractAction {
  private final ArrayList<JTextField> inputFields;
  @NotNull
  private final JFrame prevFrame;

  public StartStepMode(@NotNull final JFrame prevFrame, final ArrayList<JTextField> array) {
    this.prevFrame = prevFrame;
    this.inputFields = array;
  }

  @Override
  public void actionPerformed(@NotNull final ActionEvent e) {
    Statistics.countOfDevices = Integer.parseInt(inputFields.get(0).getText());
    Statistics.countOfClients = Integer.parseInt(inputFields.get(1).getText());
    Statistics.workTime = Integer.parseInt(inputFields.get(2).getText());
    Statistics.sizeOfBuffer = Integer.parseInt(inputFields.get(3).getText());
    Statistics.minimum = Double.parseDouble(inputFields.get(4).getText());
    Statistics.maximum = Double.parseDouble(inputFields.get(5).getText());
    Statistics.lambda = Double.parseDouble(inputFields.get(6).getText());
    createStepModeFrame();
  }

  private void createStepModeFrame() {
    prevFrame.setVisible(false);
    final Controller controller = new Controller();
    final Waveform waveform = new Waveform(controller);
    final StepModeFrame newFrame = new StepModeFrame(controller, waveform);
    newFrame.start();
  }
}

