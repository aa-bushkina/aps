package org.course.statistic;

import lombok.Getter;

@Getter
public class ClientStatistics {
  private int generatedTasksCount;
  private int canceledTasksCount;
  private double totalTime;
  private double totalBufferedTime;
  private double totalDeviceTime;
  private double squaredTotalBufferedTime;
  private double squaredTotalDeviceTime;

  public ClientStatistics() {
    generatedTasksCount = 0;
    canceledTasksCount = 0;
    totalTime = 0;
    totalBufferedTime = 0;
    squaredTotalBufferedTime = 0;
  }

  public void incrementGeneratedTask() {
    generatedTasksCount++;
  }

  public void incrementCanceledTask() {
    canceledTasksCount++;
  }

  public void addTime(double time) {
    totalTime += time;
  }

  public void addBufferedTime(final double time) {
    totalBufferedTime += time;
    squaredTotalBufferedTime += time * time;
  }

  public void addDeviceTime(final double time) {
    totalDeviceTime += time;
    squaredTotalDeviceTime += time * time;
  }

  public double getBufferDispersion() {
    return (squaredTotalBufferedTime / generatedTasksCount - Math.pow(totalBufferedTime / generatedTasksCount, 2));
  }

  public double getDeviceDispersion() {
    return (squaredTotalDeviceTime / generatedTasksCount - Math.pow(totalDeviceTime / generatedTasksCount, 2));
  }
}