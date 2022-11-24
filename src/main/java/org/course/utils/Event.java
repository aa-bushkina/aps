package org.course.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
  public Type eventType;
  public double eventTime;
  public int id;

  public Event(Type type, double time, int id) {
    this.eventType = type;
    this.eventTime = time;
    this.id = id;
  }

  public Event(Type type, double time) {
    this.eventType = type;
    this.eventTime = time;
    this.id = -1;
  }

  public static int compare(Event l, Event r) {
    if (l.eventTime < r.eventTime) {
      return -1;
    } else if (l.eventTime > r.eventTime) {
      return 1;
    } else if (l.eventType.ordinal() > r.eventType.ordinal()) {
      return -1;
    }
    return 0;
  }
}
