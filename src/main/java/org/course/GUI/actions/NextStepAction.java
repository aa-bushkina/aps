package org.course.GUI.actions;

import org.course.application.Controller;
import org.course.application.Order;
import org.course.application.events.Event;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class NextStepAction extends AbstractAction {
  @NotNull
  private final Controller controller;
  @NotNull
  private final DefaultTableModel bufferTableModel;
  @NotNull
  private final DefaultTableModel resultsTable;
  public static Event event = null;
  static double time = 0;

  public NextStepAction(@NotNull final Controller controller,
                        @NotNull final DefaultTableModel bufferTable,
                        @NotNull final DefaultTableModel resultsTable) {
    this.controller = controller;
    this.bufferTableModel = bufferTable;
    this.resultsTable = resultsTable;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    bufferTableModel.setValueAt("", controller.getBuffer().getInsertIndex(), 1);
    bufferTableModel.setValueAt("", controller.getBuffer().getFetchIndex(), 3);

    event = controller.stepMode();
    time = controller.getCurrentTime();

    for (int i = 0; i < controller.getBuffer().getCapacity(); i++) {
      Order order = controller.getBuffer().getOrders().get(i);
      if (order == null) {
        bufferTableModel.setValueAt("", i, 2);
      } else {
        bufferTableModel.setValueAt(order.orderId(), i, 2);
      }
    }
    bufferTableModel.setValueAt("-->", controller.getBuffer().getInsertIndex(), 1);
    bufferTableModel.setValueAt("<--", controller.getBuffer().getFetchIndex(), 3);

    resultsTable.addRow(new Object[]{
      "",
      controller.getCurrentTime(),
      event.eventType,
      controller.getStatistics().getCompletedOrdersCount(),
      "",
      ""});
  }
}
