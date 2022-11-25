package org.course.application.components;

import lombok.Getter;
import org.course.application.Order;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.StatController;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

@Getter
public class Distributor {
  private final List<RestaurantDevice> devices;
  private final Buffer buffer;
  private Random generator;
  private double maxRand;
  private int currentIndex;
  private final StatController statistics;

  public Distributor(@NotNull final Buffer buffer,
                     @NotNull final List<RestaurantDevice> devices,
                     @NotNull final StatController statistics) {
    this.buffer = buffer;
    this.devices = devices;
    this.generator = new Random();
    this.maxRand = generator.nextDouble();
    this.statistics = statistics;
  }
  public Event sendOrderToDevice(final double currentTime) {
    findFreeDeviceIndex();
    RestaurantDevice currentDevice = devices.get(currentIndex);
    if (currentDevice.isFree() && !buffer.isEmpty()) {
      final Order order = buffer.getOrder();
      devices.get(currentIndex).setCurrentOrder(order);
      devices.get(currentIndex).setOrderStartTime(currentTime);
      return new Event(Type.Completed, currentTime
        + getDeviceReleaseTime(), currentDevice.getDeviceId());
    }
    return null;
  }

  public double getDeviceReleaseTime() {
    return StatController.minimum + generator.nextDouble() * (StatController.maximum - StatController.minimum);
  }

  private void findFreeDeviceIndex() {
    while (!devices.get(currentIndex).isFree()) {
      currentIndex++;
      if (currentIndex == statistics.getDevicesCount()) {
        currentIndex = 0;
        return;
      }
    }
  }
}
