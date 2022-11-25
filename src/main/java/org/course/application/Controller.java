package org.course.application;

import lombok.Getter;
import org.course.application.components.*;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.StatController;

import java.util.ArrayList;

@Getter
public class Controller {
  public static StatController statistics = StatController.getInstance();
  private final int requiredOrdersCount = StatController.countOfRequiredOrders;
  private double currentTime;
  /*  private int currentIndex;*/

  private final Buffer buffer;
  private final Terminal terminal;
  private final Distributor distributor;
  private final ArrayList<Client> clients;
  private final ArrayList<RestaurantDevice> devices;
  private ArrayList<Event> events;

/*  private void findFreeDeviceIndex() {
    while (!devices.get(currentIndex).isFree()) {
      currentIndex++;
      if (currentIndex == statistics.getDevicesCount()) {
        currentIndex = 0;
        return;
      }
    }
  }*/

  private void initEvents() {
    events = new ArrayList<>();
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      events.add(new Event(Type.Generated, terminal.getNextOrderGenerationTime(), i));
    }
    if (events.size() > 0) {
      events.sort(Event::compare);
    }
  }

  public Controller() {
    //currentIndex = 0;
    currentTime = 0;
    buffer = new Buffer(statistics.getBufferSize());
    devices = new ArrayList<>(statistics.getDevicesCount());
    for (int i = 0; i < statistics.getDevicesCount(); i++) {
      devices.add(new RestaurantDevice(i));
    }
    distributor = new Distributor(buffer, devices, statistics);
    clients = new ArrayList<>(statistics.getClientsCount());
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      clients.add(new Client(i));
    }
    terminal = new Terminal(buffer, clients);
    initEvents();
  }

  public void autoMode() {
    while (!events.isEmpty()) {
      stepMode();
    }
  }

  public Event stepMode() {
    final Event currentEvent = events.remove(0);
    //System.out.println(currentEvent.eventTime + currentEvent.eventType.toString());
    final Type currentType = currentEvent.eventType;
    final int currentId = currentEvent.id;
    currentTime = currentEvent.eventTime;
    if (currentType == Type.Generated) {
      if (statistics.getTotalOrdersCount() < requiredOrdersCount) {
        terminal.addOrderToBuffer(clients.get(currentId).generateOrder(currentTime));
        events.add(new Event(Type.Generated,
          currentTime + terminal.getNextOrderGenerationTime(),
          currentId));
        events.add(new Event(Type.Unbuffered, currentTime));
        statistics.orderGenerated(currentId);
        if (events.size() > 0) {
          events.sort(Event::compare);
        }
      }
    } else if (currentType == Type.Unbuffered) {
      final Event newEvent = distributor.sendOrderToDevice(currentTime);
      if (newEvent != null) {
        events.add(newEvent);
        events.sort(Event::compare);
      }
    } else if (currentType == Type.Completed) {
      RestaurantDevice currentDevice = devices.get(currentId);

      if (currentDevice.getCurrentOrder() != null) {
        statistics.taskCompleted(currentDevice.getCurrentOrder().clientId(),
          currentTime - currentDevice.getCurrentOrder().startTime(),
          currentTime - currentDevice.getOrderStartTime());
        devices.get(currentId).setCurrentOrder(null);
        devices.get(currentId).setOrderStartTime(currentTime);
        events.add(new Event(Type.Unbuffered, currentTime));
        events.sort(Event::compare);
      }
    }
    return currentEvent;
  }

  public StatController getStatistics() {
    return statistics;
  }
}

