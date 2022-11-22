package org.course.application.components;

import lombok.Getter;
import org.course.application.Order;

@Getter
public class Client {
  final private int clientId;
  private int ordersCount;

  public Client(final int clientId) {
    this.clientId = clientId;
    ordersCount = 0;
  }

  public Order generateOrder(final double currentTime) {
    ordersCount++;
    return new Order(clientId, String.valueOf(clientId) + '-' + ordersCount, currentTime);
  }
}
