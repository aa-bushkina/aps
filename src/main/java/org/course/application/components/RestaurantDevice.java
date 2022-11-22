package org.course.application.components;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Order;

import java.util.Random;

@Getter
@Setter
public class RestaurantDevice {

  private int deviceId;
  private Order currentOrder;
  private double orderStartTime;
  private Random generator;
  private double maxRand;

  public RestaurantDevice(final int deviceId) {
    this.orderStartTime = 0;
    this.deviceId = deviceId;
    this.currentOrder = null;
  }

  /*public double setNextOrder(Order order, double currentTime) {
    taskStartTime = currentTime;
    currentOrder = order;
    double i = (StatController.maximum - StatController.minimum) / maxRand;
    return StatController.minimum + generator.nextDouble() * i;
  }*/

  public boolean isFree() {
    return (currentOrder == null);
  }
}
