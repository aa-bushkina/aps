package org.course.application;

import lombok.Getter;
import org.course.application.components.*;
import org.course.statistic.StatController;
import org.course.utils.Event;
import org.course.utils.Type;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
public class Controller {
  public static StatController stat = StatController.getInstance();
  private final int clientsCount = stat.getClientsCount();
  private final int devicesCount = stat.getDevicesCount();
  private final int bufferSize = stat.getBufferSize();
  private final int requiredOrdersCount;
  private double currentTime = 0;
  private int currentIndex = 0;

  private final Buffer buffer;
  private final Terminal terminal;
  private final Distributor distributor;
  private final ArrayList<Client> clients;
  private final ArrayList<RestaurantDevice> devices;
  private ArrayList<Event> events;

  private void findFreeDeviceIndex() {
    final int tmp = currentIndex;
    while (!devices.get(currentIndex).isFree()) {
      currentIndex++;
      if (currentIndex == devicesCount) {
        currentIndex = 0;
      }
      if (currentIndex == tmp) {
        break;
      }
    }
  }

  private void initEvents() {
    events = new ArrayList<>();
    for (int i = 0; i < clientsCount; i++) {
      events.add(new Event(Type.Generate, terminal.getNextOrderGenerationTime(), i));
    }
    if (events.size() > 0) {
      events.sort(Event::compare);
    }
  }


  public Controller() {
    requiredOrdersCount = StatController.countOfRequiredOrders;
    buffer = new Buffer(bufferSize);
    devices = new ArrayList<>(devicesCount);
    for (int i = 0; i < devicesCount; i++) {
      devices.add(new RestaurantDevice(i));
    }
    distributor = new Distributor(buffer, devices);
    clients = new ArrayList<>(clientsCount);
    for (int i = 0; i < clientsCount; i++) {
      clients.add(new Client(i));
    }
    terminal = new Terminal(buffer, clients);
    initEvents();
  }

  public StatController getStat() {
    return stat;
  }

  public void autoMode() {
    while (!events.isEmpty()) {
      stepMode();
    }
  }


  public Event stepMode() {
    final Event event = events.remove(0);
    currentTime = event.eventTime;
    final Type type = event.eventType;
    final int clientId = event.source;
    if (type == Type.Generate) {
      if (stat.getTotalOrdersCount() < requiredOrdersCount) {

        buffer.addOrder(clients.get(clientId).generateOrder(currentTime));
        events.add(new Event(Type.Generate, currentTime
          + terminal.getNextOrderGenerationTime(), clientId));
        events.add(new Event(Type.Unbuffer, currentTime));
        stat.orderGenerated(clientId);
        if (events.size() > 0) {
          events.sort(Event::compare);
        }
      }
    } else if (type == Type.Unbuffer) {
      findFreeDeviceIndex();
      if (devices.get(currentIndex).isFree()) {
        RestaurantDevice currentDevice = devices.get(currentIndex);
        devices.set(currentIndex, null);
        if (buffer.isEmpty() || !currentDevice.isFree()) {
          devices.set(currentIndex, currentDevice);
        } else {
          Order order = buffer.getOrder();
          events.add(new Event(Type.Complete, currentTime
            + distributor.getDeviceReleaseTime(), currentDevice.getDeviceId()));
          currentDevice.setCurrentOrder(order);
          currentDevice.setOrderStartTime(currentTime);
          if (events.size() > 0) {
            events.sort(Event::compare);
          }
          devices.set(currentIndex, currentDevice);
          currentIndex++;
          if (currentIndex == devicesCount) {
            currentIndex = 0;
          }
        }
      }
    } else if (type == Type.Complete) {
      RestaurantDevice currentDevice = null;
      for (RestaurantDevice device : devices) {
        if (device.getDeviceId() == clientId) {
          currentDevice = device;
          break;
        }
      }
      int pos = devices.indexOf(currentDevice);
      devices.set(pos, null);
      assert currentDevice != null;/////////////////////////////
      if (currentDevice.getCurrentOrder()!=null) {
        stat.taskCompleted(currentDevice.getCurrentOrder().clientId(), clientId,
          currentTime - currentDevice.getCurrentOrder().startTime(),
          currentTime - currentDevice.getOrderStartTime());
        // terminal.getNextOrderGenerationTime();
        currentDevice.setCurrentOrder(null);
        currentDevice.setOrderStartTime(currentTime);
        devices.set(pos, currentDevice);
        events.add(new Event(Type.Unbuffer, currentTime));
        events.sort(Event::compare);
      }
    }
    return event;
  }
}

