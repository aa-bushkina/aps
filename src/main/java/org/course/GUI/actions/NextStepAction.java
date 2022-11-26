package org.course.GUI.actions;

import org.course.application.Controller;
import org.course.application.Order;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class NextStepAction extends AbstractAction {
  @NotNull
  private final Controller controller;
  @NotNull
  private final DefaultTableModel bufferTable;
  @NotNull
  private final DefaultTableModel resultsTable;
  @NotNull
  private final DefaultTableModel devicesTable;
  public static Event event = null;
  static double time = 0;

  public NextStepAction(@NotNull final Controller controller,
                        @NotNull final DefaultTableModel bufferTable,
                        @NotNull final DefaultTableModel resultsTable,
                        @NotNull final DefaultTableModel devicesTable) {
    this.controller = controller;
    this.bufferTable = bufferTable;
    this.resultsTable = resultsTable;
    this.devicesTable = devicesTable;
  }

  public NextStepAction(@NotNull final NextStepAction nextStepAction) {
    this.controller = nextStepAction.controller;
    this.bufferTable = nextStepAction.bufferTable;
    this.resultsTable = nextStepAction.resultsTable;
    this.devicesTable = nextStepAction.devicesTable;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    bufferTable.setValueAt("", controller.getBuffer().getInsertIndex(), 1);
    bufferTable.setValueAt("", controller.getBuffer().getFetchIndex(), 3);

    event = controller.stepMode();
    time = controller.getCurrentTime();

    for (int i = 0; i < controller.getBuffer().getCapacity(); i++) {
      Order order = controller.getBuffer().getOrders().get(i);
      if (order == null) {
        bufferTable.setValueAt("", i, 2);
      } else {
        bufferTable.setValueAt(order.orderId(), i, 2);
      }
    }
    bufferTable.setValueAt("-->", controller.getBuffer().getInsertIndex(), 1);
    bufferTable.setValueAt("<--", controller.getBuffer().getFetchIndex(), 3);

    for (int i = 0; i < controller.getDevices().size(); i++) {
      Order order = controller.getDevices().get(i).getCurrentOrder();
      if (order == null) {
        devicesTable.setValueAt("", i, 1);
      } else {
        devicesTable.setValueAt(order.orderId(), i, 1);
      }
    }
    String element;
    if (event.eventType == Type.Generated) {
      element = "И" + event.id;
    } else {
      element = "П" + event.id;
    }

    resultsTable.addRow(new Object[]{
      controller.getCurrentTime(),
      element,
      event.eventType,
      event.orderId,
      controller.getStatistics().getCompletedOrdersCount(),
      controller.getStatistics().getCanceledOrdersCount()});
  }
}
