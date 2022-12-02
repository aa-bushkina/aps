package org.course.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Statistics {
  public static int countOfDevices;
  public static int countOfClients;
  public static int workTime;
  public static int sizeOfBuffer;
  public static double minimum;
  public static double maximum;
  public static double lambda;

  private int devicesCount;
  private int clientsCount;
  private int bufferSize;
  private int totalOrdersCount;
  private int completedOrdersCount;
  private ArrayList<Double> devicesWorkTime;
  private ArrayList<ClientStatistics> clientsStats;

  private Statistics(final int devicesCount, final int clientsCount) {
    this.devicesCount = devicesCount;
    this.clientsCount = clientsCount;
    this.bufferSize = sizeOfBuffer;
    this.totalOrdersCount = 0;
    this.completedOrdersCount = 0;
    this.devicesWorkTime = new ArrayList<>(this.devicesCount);
    for (int i = 0; i < this.devicesCount; i++) {
      devicesWorkTime.add(i, 0.0);
    }
    this.clientsStats = new ArrayList<>(this.clientsCount);
    for (int i = 0; i < this.clientsCount; i++) {
      clientsStats.add(i, new ClientStatistics());
    }
  }

  public static Statistics getInstance() {
    return new Statistics(countOfDevices, countOfClients);
  }

  public void orderGenerated(final int srcId) {
    clientsStats.get(srcId).incrementGeneratedTask();
    totalOrdersCount++;
  }

  public int getCanceledOrdersCount() {
    int sum = 0;
    for (ClientStatistics stat : clientsStats) {
      sum += stat.getCanceledOrdersCount();
    }
    return sum;
  }

  public void taskCanceled(final int clientId, final double usedTime) {
    clientsStats.get(clientId).incrementCanceledTask();
    taskCompleted(clientId, usedTime, 0);
  }

  public void taskCompleted(final int clientId, final double usedTime, final double processedTime) {
    clientsStats.get(clientId).addTotalTime(usedTime);
    clientsStats.get(clientId).addOnBufferTime(usedTime - processedTime);
    completedOrdersCount++;
  }

  public void addDeviceByClientTime(final int clientId, final double time) {
    clientsStats.get(clientId).addOnDeviceTime(time);
  }

  public void addEachDeviceTime(final int deviceId, final double time) {
    devicesWorkTime.set(deviceId, devicesWorkTime.get(deviceId) + time);
  }
}
