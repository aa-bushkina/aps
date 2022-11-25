package org.course.GUI.actions;

import org.course.GUI.frames.ChooseModeFrame;
import org.course.GUI.frames.StartFrame;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SetDataAction extends AbstractAction {
  @NotNull
  private final ArrayList<JTextField> startDataFields;
  @NotNull
  private final StartFrame startFrame;

  public SetDataAction(@NotNull final StartFrame startFrame) {
    this.startFrame = startFrame;
    this.startDataFields = startFrame.getStartDataFields();
  }

  @Override
  public void actionPerformed(@NotNull final ActionEvent e) {
    Statistics.countOfRestaurantDevices = Integer.parseInt(startDataFields.get(0).getText());
    Statistics.countOfClients = Integer.parseInt(startDataFields.get(1).getText());
    Statistics.countOfRequiredOrders = Integer.parseInt(startDataFields.get(2).getText());
    Statistics.sizeOfBuffer = Integer.parseInt(startDataFields.get(3).getText());
    Statistics.minimum = Double.parseDouble(startDataFields.get(4).getText());
    Statistics.maximum = Double.parseDouble(startDataFields.get(5).getText());
    Statistics.lambda = Double.parseDouble(startDataFields.get(6).getText());
    createChooseFrame();
  }

  private void createChooseFrame() {
    startFrame.hide();
    ChooseModeFrame modeFrame = new ChooseModeFrame();
    modeFrame.start();
  }
}

