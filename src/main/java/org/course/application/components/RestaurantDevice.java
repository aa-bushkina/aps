package org.course.application.components;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Order;
import org.jetbrains.annotations.NotNull;

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

  public boolean isFree() {
    return (currentOrder == null);
  }
}
