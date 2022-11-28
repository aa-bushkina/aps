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
  private final ArrayList<JTextField> startDataFields;
  @NotNull
  private final JFrame prevFrame;

  public StartStepMode(@NotNull final JFrame prevFrame, final ArrayList<JTextField> array) {
    this.prevFrame = prevFrame;
    this.startDataFields = array;
  }

  @Override
  public void actionPerformed(@NotNull final ActionEvent e) {
    Statistics.countOfRestaurantDevices = Integer.parseInt(startDataFields.get(0).getText());
    Statistics.countOfClients = Integer.parseInt(startDataFields.get(1).getText());
    Statistics.countOfOrders = Integer.parseInt(startDataFields.get(2).getText());
    Statistics.sizeOfBuffer = Integer.parseInt(startDataFields.get(3).getText());
    Statistics.minimum = Double.parseDouble(startDataFields.get(4).getText());
    Statistics.maximum = Double.parseDouble(startDataFields.get(5).getText());
    Statistics.lambda = Double.parseDouble(startDataFields.get(6).getText());
    createStepModeFrame();
  }

  private void createStepModeFrame() {
    prevFrame.setVisible(false);
    Controller controller = new Controller();
    Waveform waveform = new Waveform(controller);
    StepModeFrame newFrame = new StepModeFrame(controller, waveform);
    newFrame.start();
  }
}

