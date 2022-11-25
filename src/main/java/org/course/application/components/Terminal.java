package org.course.application.components;

import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.StatController;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Terminal {
  @NotNull
  final private Buffer buffer;
  @NotNull
  List<Client> clients;
  @NotNull
  private final StatController statistics;

  public Terminal(@NotNull final Buffer buffer,
                  @NotNull final List<Client> clients,
                  @NotNull final StatController statistics) {
    this.buffer = buffer;
    this.clients = clients;
    this.statistics = statistics;
  }

  public List<Event> sendOrderToBuffer(final int currentId, final double currentTime) {
    buffer.addOrder(clients.get(currentId).generateOrder(currentTime));
    List<Event> events = List.of(
      new Event(Type.Generated, currentTime + clients.get(currentId).getNextOrderGenerationTime(), currentId),
      new Event(Type.Unbuffered, currentTime));
    statistics.orderGenerated(currentId);
    return events;
  }
}
