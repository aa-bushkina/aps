package org.course.application.components;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Order;
import org.course.statistic.StatController;

import java.util.Random;

@Getter
@Setter
public class RestaurantDevice {
  private int deviceId;
  private Order currentOrder;
  private double orderStartTime;

  public RestaurantDevice(final int deviceId) {
    this.deviceId = deviceId;
    this.orderStartTime = 0;
    this.currentOrder = null;
  }

  public double getReleaseTime() {
    Random rand = new Random();
    return StatController.minimum + rand.nextDouble() * (StatController.maximum - StatController.minimum);
  }

  public boolean isFree() {
    return (currentOrder == null);
  }
}
