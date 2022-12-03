package org.course.application.components;

import lombok.Getter;
import lombok.Setter;
import org.course.application.Order;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
@Setter
public class Device {
  private final Statistics statistics;
  private int deviceId;
  private Order currentOrder;
  private double orderStartTime;

  public Device(final int deviceId, @NotNull final Statistics statistics) {
    this.deviceId = deviceId;
    this.orderStartTime = 0;
    this.currentOrder = null;
    this.statistics = statistics;
  }

  public double getReleaseTime() {
    Random rand = new Random();
    return Statistics.minimum + rand.nextDouble() * (Statistics.maximum - Statistics.minimum);
  }

  public boolean isFree() {
    return (currentOrder == null);
  }

  public void release(final double currentTime) {
    final double timeOnDevice = currentTime - orderStartTime;
    statistics.orderCompleted(currentOrder.clientId(), timeOnDevice, timeOnDevice);
    statistics.addDeviceByClientTime(currentOrder.clientId(), timeOnDevice);
    statistics.addEachDeviceTime(deviceId, timeOnDevice);
    currentOrder = null;
    orderStartTime = currentTime;
  }
}
