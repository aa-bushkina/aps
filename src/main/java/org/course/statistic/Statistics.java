package org.course.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Statistics {
  public static int countOfRestaurantDevices;
  public static int countOfClients;
  public static int countOfOrders;
  public static int sizeOfBuffer;
  public static double minimum;
  public static double maximum;
  public static double lambda;

  private int bufferSize;
  private int devicesCount;
  private int clientsCount;
  private int totalOrdersCount;
  private int completedOrdersCount;
  private double totalTime;
  private ArrayList<Double> devicesTime;
  private ArrayList<ClientStatistics> clientsStats;

  private Statistics(double totalTime, int devicesCount, int clientsCount) {
    this.devicesCount = devicesCount;
    this.clientsCount = clientsCount;
    this.totalTime = totalTime;
    this.bufferSize = sizeOfBuffer;
    this.totalOrdersCount = 0;
    this.completedOrdersCount = 0;
    this.devicesTime = new ArrayList<>(this.devicesCount);
    for (int i = 0; i < this.devicesCount; i++) {
      devicesTime.add(i, 0.0);
    }
    this.clientsStats = new ArrayList<>(this.clientsCount);
    for (int i = 0; i < this.clientsCount; i++) {
      clientsStats.add(i, new ClientStatistics());
    }
  }

  public static Statistics getInstance() {
    return new Statistics(220000, countOfRestaurantDevices, countOfClients);
  }

  public void orderGenerated(int srcId) {
    clientsStats.get(srcId).incrementGeneratedTask();
    totalOrdersCount++;
  }

  public int getCanceledOrdersCount() {
    int sum = 0;
    for (ClientStatistics stat : clientsStats) {
      sum += stat.getCanceledTasksCount();
    }
    return sum;
  }

  public void taskCanceled(int srcId, double usedTime) {
    clientsStats.get(srcId).incrementCanceledTask();
    taskCompleted(srcId, usedTime, 0);
  }

  public void taskCompleted(int srcId, double usedTime, double processedTime) {
    clientsStats.get(srcId).addTime(usedTime);
    clientsStats.get(srcId).addBufferedTime(usedTime - processedTime);
    completedOrdersCount++;
  }

  public void addDeviceTime(final int deviceId, final double time) {
    devicesTime.set(deviceId, devicesTime.get(deviceId) + time);
  }
}
