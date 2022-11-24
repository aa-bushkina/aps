package org.course.application.components;

import lombok.Getter;
import org.course.application.Controller;
import org.course.application.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class Buffer {
  @NotNull
  private final ArrayList<Order> stack;
  private int insertIndex;
  private int fetchIndex;
  private final int capacity;
  private int size;
  ArrayList<Integer> countTimeInBuffer;

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

  private void addNewIndexValue(final int index) {
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

  public Buffer(final int capacity) {
    this.capacity = capacity;
    this.size = 0;
    this.insertIndex = 0;
    this.fetchIndex = 0;
    stack = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      stack.add(null);
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
    if (isFull()) {
      incrementIndexValue();
      final int oldestInsertIndex = findOldestIndexOfElement();
      Order canceledOrder = stack.get(oldestInsertIndex);
      stack.set(oldestInsertIndex, order);
      addNewIndexValue(oldestInsertIndex);
      Controller.statistics.taskCanceled(canceledOrder.clientId(),
        order.startTime() - canceledOrder.startTime());
      return;
    }
    incrementIndexValue();
    stack.set(insertIndex, order);
    addNewIndexValue(insertIndex);
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
    if (stack.get(fetchIndex) != null) {
      final Order order = stack.get(fetchIndex);
      stack.set(fetchIndex, null);
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
      if (stack.get(fetchIndex) != null) {
        order = stack.get(fetchIndex);
        stack.set(fetchIndex, null);
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
}

