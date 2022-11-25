package org.course.application.components;

import lombok.Getter;
import org.course.application.Controller;
import org.course.application.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class Buffer {
  @NotNull
  private final ArrayList<Order> orders;
  private int insertIndex;
  private int fetchIndex;
  private final int capacity;
  private int size;
  ArrayList<Integer> countTimeInBuffer;

  public Buffer(final int capacity) {
    this.capacity = capacity;
    this.size = 0;
    this.insertIndex = 0;
    this.fetchIndex = 0;
    orders = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      orders.add(null);
    }
    countTimeInBuffer = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      countTimeInBuffer.add(0);
    }
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean isFull() {
    return size == capacity;
  }

  public void addOrder(@NotNull final Order order) {
    incrementIndexValue();
    if (isFull()) {
      final int oldestInsertIndex = findOldestIndexOfElement();
      Order canceledOrder = orders.get(oldestInsertIndex);
      orders.set(oldestInsertIndex, order);
      initializeIndexValue(oldestInsertIndex);
      makeStatistics(canceledOrder, order);
      return;
    }
    orders.set(insertIndex, order);
    initializeIndexValue(insertIndex);
    insertIndex++;
    if (insertIndex == capacity) {
      insertIndex = 0;
    }
    size++;
  }

  public Order getOrder() {
    if (isEmpty()) {
      return null;
    }
    if (orders.get(fetchIndex) != null) {
      final Order order = orders.get(fetchIndex);
      orders.set(fetchIndex, null);
      clearIndexValue(fetchIndex);
      fetchIndex++;
      if (fetchIndex == capacity) {
        fetchIndex = 0;
      }
      size--;
      return order;
    }
    Order order;
    final int currentIndex = fetchIndex;
    fetchIndex++;
    if (fetchIndex == capacity) {
      fetchIndex = 0;
    }
    while (fetchIndex != currentIndex) {
      if (orders.get(fetchIndex) != null) {
        order = orders.get(fetchIndex);
        orders.set(fetchIndex, null);
        clearIndexValue(fetchIndex);
        fetchIndex++;
        if (fetchIndex == capacity) {
          fetchIndex = 0;
        }
        size--;
        return order;
      }
      fetchIndex++;
      if (fetchIndex == capacity) {
        fetchIndex = 0;
      }
    }
    return null;
  }

  private void makeStatistics(@NotNull final Order canceledOrder, @NotNull final Order order) {
    Controller.statistics.taskCanceled(canceledOrder.clientId(),
      order.startTime() - canceledOrder.startTime());
  }

  private void incrementIndexValue() {
    for (int i = 0; i < countTimeInBuffer.size(); i++) {
      if (countTimeInBuffer.get(i) != 0) {
        countTimeInBuffer.set(i, countTimeInBuffer.get(i) + 1);
      }
    }
  }

  private void clearIndexValue(final int index) {
    countTimeInBuffer.set(index, 0);
  }

  private void initializeIndexValue(final int index) {
    countTimeInBuffer.set(index, 1);
  }

  private int findOldestIndexOfElement() {
    int maxIndex = -1;
    int maxValue = -1;
    for (int i = 0; i < countTimeInBuffer.size(); i++) {
      int times = countTimeInBuffer.get(i);
      if (times > maxValue) {
        maxValue = times;
        maxIndex = i;
      }
    }
    return maxIndex;
  }
}

