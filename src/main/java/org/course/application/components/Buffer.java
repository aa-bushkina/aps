package org.course.application.components;

import lombok.Getter;
import org.course.application.Controller;
import org.course.application.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

@Getter
public class Buffer {
  @NotNull
  private final ArrayList<Order> orders;
  private final int capacity;
  LinkedList<Integer> indexesQueue;
  private int insertIndex;
  private int fetchIndex;
  private int size;

  public Buffer(final int capacity) {
    this.capacity = capacity;
    this.size = 0;
    this.insertIndex = 0;
    this.fetchIndex = 0;
    orders = new ArrayList<>(capacity);
    for (int i = 0; i < capacity; i++) {
      orders.add(null);
    }
    indexesQueue = new LinkedList<>();
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean isFull() {
    return size == capacity;
  }

  public void addOrder(@NotNull final Order order) {
    if (isFull()) {
      if (indexesQueue.isEmpty()) {
        throw new RuntimeException("Indexes Queue can't be empty!");
      }
      insertIndex = indexesQueue.pollFirst();
      Order canceledOrder = orders.get(insertIndex);
      makeStatistics(canceledOrder, order);
    } else {
      size++;
      while (orders.get(insertIndex) != null) {
        insertIndex++;
        if (insertIndex == capacity) {
          insertIndex = 0;
        }

      }
    }
    orders.set(insertIndex, order);
    indexesQueue.addLast(insertIndex);
    insertIndex++;
    if (insertIndex == capacity) {
      insertIndex = 0;
    }
  }

  public Order getOrder() {
    if (isEmpty()) {
      throw new RuntimeException("Buffer is empty!");
    }
    if (orders.get(fetchIndex) != null) {
      final Order order = orders.get(fetchIndex);
      orders.set(fetchIndex, null);
      indexesQueue.remove((Integer) fetchIndex);
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
        indexesQueue.remove((Integer) fetchIndex);
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
    throw new RuntimeException("Buffer is empty!");
  }

  private void makeStatistics(@NotNull final Order canceledOrder, @NotNull final Order order) {
    Controller.statistics.orderCanceled(canceledOrder.clientId(),
      order.startTime() - canceledOrder.startTime());
  }
}

