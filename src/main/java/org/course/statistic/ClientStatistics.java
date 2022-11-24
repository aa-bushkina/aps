package org.course.statistic;

import lombok.Getter;

@Getter
public class ClientStatistics {
  private int generatedTasksCount;
  private int canceledTasksCount;
  private double totalTime;
  private double squaredTotalTime;
  private double totalBufferedTime;
  private double squaredTotalBufferedTime;

  public ClientStatistics() {
    generatedTasksCount = 0;
    canceledTasksCount = 0;
    totalTime = 0;
    squaredTotalTime = 0;
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
    squaredTotalTime += time * time;
  }

  public void addBufferedTime(double time) {
    totalBufferedTime += time;
    squaredTotalBufferedTime += time * time;
  }

  public double getBufferDispersion() {
    return (squaredTotalBufferedTime / generatedTasksCount - Math.pow(totalBufferedTime / generatedTasksCount, 2));
  }

  public double getTotalDispersion() {
    return (squaredTotalTime / generatedTasksCount - Math.pow(totalTime / generatedTasksCount, 2));
  }
}