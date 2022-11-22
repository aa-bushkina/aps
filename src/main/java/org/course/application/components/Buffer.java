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

  public Buffer(final int capacity) {
    this.capacity = capacity;
    this.size = 0;
    this.insertIndex = 0;
    stack = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      stack.add(null);
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
      final int oldestInsertIndex = (insertIndex == 0) ? capacity - 1 : insertIndex - 1;
      Order canceledOrder = stack.get(oldestInsertIndex);
      stack.set(oldestInsertIndex, order);
      Controller.stat.taskCanceled(canceledOrder.clientId(),
        order.startTime() - canceledOrder.startTime());
      return;
    }
    stack.set(insertIndex, order);
    insertIndex++;
    if (insertIndex == capacity) {
      insertIndex = 0;
    }
    size++;
  }

  public Order getOrder() {
    if (isEmpty()) {
      throw new RuntimeException("Buffer is empty");
    }
    if (stack.get(fetchIndex) == null) {
      fetchIndex++;
      if (fetchIndex == capacity) {
        fetchIndex = 0;
      }
      return null;
    }
    final Order order = stack.get(fetchIndex);
    stack.set(fetchIndex, null);
    fetchIndex++;
    if (fetchIndex == capacity) {
      fetchIndex = 0;
    }
    size--;
    return order;
  }
}

