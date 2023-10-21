package org.course.application;

import lombok.Getter;
import org.course.application.components.*;
import org.course.application.events.Event;
import org.course.application.events.Type;
import org.course.statistic.Statistics;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Controller {
  public static Statistics statistics = Statistics.getInstance();
  private final double workTime = Statistics.workTime;
  private Event currentEvent;
  private double currentTime;

  private final Buffer buffer;
  private final Terminal terminal;
  private final Distributer distributer;
  private final ArrayList<Client> clients;
  private final ArrayList<Device> devices;
  private ArrayList<Event> events;

  private void initEvents() {
    events = new ArrayList<>();
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      events.add(new Event(Type.Generated, clients.get(i).getNextOrderGenerationTime(), (i + "-" + 0), i));
    }
    if (events.size() > 0) {
      events.sort(Event::compare);
    }
  }

  public Controller() {
    currentTime = 0;
    buffer = new Buffer(statistics.getBufferSize());
    devices = new ArrayList<>(statistics.getDevicesCount());
    for (int i = 0; i < statistics.getDevicesCount(); i++) {
      devices.add(new Device(i, statistics));
    }
    distributer = new Distributer(buffer, devices, statistics);
    clients = new ArrayList<>(statistics.getClientsCount());
    for (int i = 0; i < statistics.getClientsCount(); i++) {
      clients.add(new Client(i));
    }
    terminal = new Terminal(buffer, clients, statistics);
    initEvents();
  }

  public void stepMode() {
    currentEvent = events.remove(0);
    final Type currentType = currentEvent.eventType;
    final int currentId = currentEvent.id;
    currentTime = currentEvent.eventTime;
    if (currentType == Type.Generated) {
      List<Event> newEvents = terminal.putOrderToBuffer(currentId, currentTime);
      if (!newEvents.isEmpty()) {
        events.addAll(newEvents);
        events.sort(Event::compare);
      }
    } else if (currentType == Type.Unbuffered) {
      final Event newEvent = distributer.sendOrderToDevice(currentTime, currentEvent);
      if (newEvent != null) {
        events.add(newEvent);
        events.sort(Event::compare);
      }
    } else if (currentType == Type.Completed) {
      devices.get(currentId).release(currentTime);
      events.add(new Event(Type.Unbuffered, currentTime, null));
      events.sort(Event::compare);
    }
  }

  public Statistics getStatistics() {
    return statistics;
  }
}

