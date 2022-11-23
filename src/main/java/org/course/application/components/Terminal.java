package org.course.application.components;

import org.course.application.Order;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.course.statistic.StatController.lambda;

public class Terminal {
  @NotNull
  final private Buffer buffer;
  @NotNull
  List<Client> clients;

  public Terminal(@NotNull final Buffer buffer, @NotNull final List<Client> clients) {
    this.buffer = buffer;
    this.clients = clients;
  }

  public void addOrderToBuffer(@NotNull final Order order) {
    buffer.addOrder(order);
  }

  public double getNextOrderGenerationTime() {
    return (-1.0 / lambda) * Math.log(Math.random());
  }
}
