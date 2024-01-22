package com.timefusion.ui.calendar;

import com.timefusion.TimeFusionApp;
import com.timefusion.localStorage.Entities.EventNature;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.sync.SyncUtil;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;

public class DisplaySync {

  public static List<Integer> getNoneDisplayedEventsId() {
    List<Integer> allEventsId = SyncUtil.getLocalEventsIds();
    ObservableList<Calendar> calendar = CalendarView.getCalendars();
    List<Appointment> displayedEvents = calendar.get(0).getAppointments();
    List<Integer> displayedEventsId = new ArrayList<>();
    if (displayedEvents.size() > 0) {
      for (Appointment event : displayedEvents) {
        int eventId = event.getEventEntity().getId();
        displayedEventsId.add(eventId);
      }
    }

    if (displayedEventsId.size() > 0) {
      allEventsId.removeAll(displayedEventsId);
    }
    return allEventsId;
  }

  private static List<Integer> getEventsToDisplayIds() {
    List<Integer> allEventsId = new ArrayList<>();
    List<Integer> displaybleEventsId = new ArrayList<>();
    allEventsId = SyncUtil.getLocalEventsIds();
    if (allEventsId.size() > 0) {
      for (int eventId : allEventsId) {
        EventsEntity event = EventsEntity.getEventEntityById(eventId);
        if (
          event.getNature().equals(EventNature.ADDED) ||
          event.getNature().equals(EventNature.INVITED) ||
          event.getNature().equals(EventNature.UNCHANGED)
        ) {
          displaybleEventsId.add(eventId);
        }
      }
    }
    return displaybleEventsId;
  }

  private static List<Integer> getEventsToNotDisplayIds() {
    List<Integer> allEventsId = SyncUtil.getLocalEventsIds();
    List<Integer> notDisplaybleEventsId = new ArrayList<>();

    if (allEventsId.size() > 0) {
      for (int eventId : allEventsId) {
        EventsEntity event = EventsEntity.getEventEntityById(eventId);
        if (
          event.getNature().equals(EventNature.DELETED) ||
          event.getNature().equals(EventNature.DENIED)
        ) {
          notDisplaybleEventsId.add(eventId);
        }
      }
    }
    return notDisplaybleEventsId;
  }

  private static boolean displayedMissingAppointments() {
    boolean needToUpdate = false;
    List<Integer> noneDisplayedEventsId = getNoneDisplayedEventsId();
    List<Integer> eventsToDisplayIds = getEventsToDisplayIds();
    noneDisplayedEventsId.retainAll(eventsToDisplayIds);
    if (noneDisplayedEventsId.size() > 0) {
      needToUpdate = true;
      for (int eventId : noneDisplayedEventsId) {
        EventsEntity event = EventsEntity.getEventEntityById(eventId);
        Appointment appointment = new Appointment(event);
        CalendarView.getCalendars().get(0).add(appointment);
      }
    }
    return needToUpdate;
  }

  private static boolean removeElementsNotNeededToBeDisplayed() {
    boolean needToUpdate = false;
    List<Integer> noneDisplayedEventsId = getNoneDisplayedEventsId();
    List<Integer> eventsToNotDisplayIds = getEventsToNotDisplayIds();
    noneDisplayedEventsId.retainAll(eventsToNotDisplayIds);
    if (noneDisplayedEventsId.size() > 0) {
      needToUpdate = true;
      for (int eventId : noneDisplayedEventsId) {
        EventsEntity event = EventsEntity.getEventEntityById(eventId);
        Appointment appointment = new Appointment(event);
        CalendarView.getCalendars().get(0).remove(appointment);
      }
    }
    return needToUpdate;
  }

  //Evenements affichés qui ne le devraient pas
  private static boolean removeAmbiguousAppointments() {
    boolean needToUpdate = false;
    List<Integer> allEventsId = SyncUtil.getLocalEventsIds();
    ObservableList<Calendar> calendar = CalendarView.getCalendars();
    List<Appointment> displayedEvents = calendar.get(0).getAppointments();
    List<Integer> eventsToNotDisplayIds = getEventsToNotDisplayIds();
    if (displayedEvents.size() > 0) {
      for (Appointment event : displayedEvents) {
        int eventId = event.getEventEntity().getId();
        if (
          eventsToNotDisplayIds.contains(eventId) ||
          !allEventsId.contains(eventId)
        ) {
          needToUpdate = true;
          CalendarView.getCalendars().get(0).remove(event);
        }
      }
    }
    return needToUpdate;
  }

  public static void synchronizeDisplay() {
    Platform.runLater(() -> {
      if (
        displayedMissingAppointments() ||
        removeElementsNotNeededToBeDisplayed() ||
        removeAmbiguousAppointments()
      ) {
        TimeFusionApp.getWeekView().update();
      }
    });
  }
}
