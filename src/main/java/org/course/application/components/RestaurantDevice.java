package org.course.application.components;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Order;
import org.course.statistic.StatController;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
@Setter
public class RestaurantDevice {
  private int deviceId;
  private Order currentOrder;
  private double orderStartTime;
  private final StatController statistics;

  public RestaurantDevice(final int deviceId,
                          @NotNull final StatController statistics) {
    this.deviceId = deviceId;
    this.orderStartTime = 0;
    this.currentOrder = null;
    this.statistics = statistics;
  }

  public double getReleaseTime() {
    Random rand = new Random();
    return StatController.minimum + rand.nextDouble() * (StatController.maximum - StatController.minimum);
  }

  public boolean isFree() {
    return (currentOrder == null);
  }

  public void release(final double currentTime) {
    if (currentOrder != null) {
      statistics.taskCompleted(currentOrder.clientId(),
        currentTime - orderStartTime,
        currentTime - orderStartTime);
      currentOrder = null;
      orderStartTime = currentTime;
    }
  }
}
