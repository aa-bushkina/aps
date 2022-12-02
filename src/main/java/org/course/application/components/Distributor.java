package org.course.application.components;

import lombok.Getter;
import org.course.application.Order;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class Distributor {
  @NotNull
  private final List<Device> devices;
  @NotNull
  private final Buffer buffer;
  @NotNull
  private final Statistics statistics;
  private int currentIndex;

  public Distributor(@NotNull final Buffer buffer,
                     @NotNull final List<Device> devices,
                     @NotNull final Statistics statistics) {
    this.buffer = buffer;
    this.devices = devices;
    this.statistics = statistics;
  }

  public Event sendOrderToDevice(final double currentTime, @NotNull final Event currentEvent) {
    findFreeDeviceIndex();
    Device currentDevice = devices.get(currentIndex);
    currentEvent.setOrderId("");
    if (currentDevice.isFree() && !buffer.isEmpty()) {
      currentEvent.setId(devices.get(currentIndex).getDeviceId());
      final Order order = buffer.getOrder();
      currentEvent.setOrderId(order.orderId());
      devices.get(currentIndex).setCurrentOrder(order);
      devices.get(currentIndex).setOrderStartTime(currentTime);
      return new Event(
        Type.Completed,
        currentTime + devices.get(currentIndex).getReleaseTime(),
        order.orderId(),
        currentDevice.getDeviceId());
    }
    return null;
  }

  public void findFreeDeviceIndex() {
    currentIndex = 0;
    while (!devices.get(currentIndex).isFree()) {
      currentIndex++;
      if (currentIndex == statistics.getDevicesCount()) {
        currentIndex = 0;
        return;
      }
    }
  }
}
