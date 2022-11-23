package org.course.application.components;

import lombok.Getter;
import org.course.application.Order;
import org.course.statistic.StatController;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Getter
public class Distributor {
  private final List<RestaurantDevice> devices;
  private final Buffer buffer;
  private Random generator;
  private double maxRand;

  public Distributor(@NotNull final Buffer buffer,
                     @NotNull final List<RestaurantDevice> devices) {
    this.buffer = buffer;
    this.devices = devices;
    this.generator = new Random();
    this.maxRand = generator.nextDouble();
  }

  public double getDeviceReleaseTime() {
    double i = (StatController.maximum - StatController.minimum) / maxRand;
    return StatController.minimum + generator.nextDouble() * i;
  }

  public Optional<Order> getOrderFromBuffer() {
    return Optional.ofNullable(buffer.getOrder());
  }

  public boolean isExistOrdersInBuffer() {
    return !buffer.isEmpty();
  }

}
