package org.course.application.components;

import org.course.application.Order;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.Statistics;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Terminal {
  @NotNull
  final private Buffer buffer;
  @NotNull
  List<Client> clients;
  @NotNull
  private final Statistics statistics;

  public Terminal(@NotNull final Buffer buffer,
                  @NotNull final List<Client> clients,
                  @NotNull final Statistics statistics) {
    this.buffer = buffer;
    this.clients = clients;
    this.statistics = statistics;
  }

  private Order receiveOrder(final int currentId, final double currentTime) {
    return clients.get(currentId).generateOrder(currentTime);
  }

  public List<Event> putOrderToBuffer(final int currentId, final double currentTime) {
    final Order currentOrder = receiveOrder(currentId, currentTime);
    buffer.addOrder(currentOrder);
    List<Event> events = List.of(
      new Event(Type.Unbuffered, currentTime, currentOrder.orderId()),
      new Event(Type.Generated, currentTime + clients.get(currentId).getNextOrderGenerationTime(),
        null, currentId));
    statistics.orderGenerated(currentId);
    return events;
  }
}
