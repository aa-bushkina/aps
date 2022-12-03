package org.course.statistic;

import lombok.Getter;

@Getter
public class ClientStatistics {
  private int generatedOrdersCount;
  private int canceledOrdersCount;
  private double totalTime;
  private double totalOnBufferTime;
  private double totalOnDeviceTime;
  private double squaredTotalOnBufferTime;
  private double squaredTotalOnDeviceTime;

  public ClientStatistics() {
    generatedOrdersCount = 0;
    canceledOrdersCount = 0;
    totalTime = 0;
    totalOnBufferTime = 0;
    totalOnDeviceTime = 0;
    squaredTotalOnBufferTime = 0;
    squaredTotalOnDeviceTime = 0;
  }

  public void incrementGeneratedOrders() {
    generatedOrdersCount++;
  }

  public void incrementCanceledOrders() {
    canceledOrdersCount++;
  }

  public void addTotalTime(final double time) {
    totalTime += time;
  }

  public void addOnBufferTime(final double time) {
    totalOnBufferTime += time;
    squaredTotalOnBufferTime += time * time;
  }

  public void addOnDeviceTime(final double time) {
    totalOnDeviceTime += time;
    squaredTotalOnDeviceTime += time * time;
  }

  public double getOnBufferDispersion() {
    return (squaredTotalOnBufferTime / generatedOrdersCount - Math.pow(totalOnBufferTime / generatedOrdersCount, 2));
  }

  public double getOnDeviceDispersion() {
    return (squaredTotalOnDeviceTime / generatedOrdersCount - Math.pow(totalOnDeviceTime / generatedOrdersCount, 2));
  }
}