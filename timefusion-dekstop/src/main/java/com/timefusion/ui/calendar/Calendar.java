package com.timefusion.ui.calendar;

import com.timefusion.ui.calendar.util.TimeInterval;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Calendar extends ModifiableObservableListBase<Appointment> {

  ObservableList<Appointment> appointments = FXCollections.observableArrayList(app ->
    new Observable[] { app.intervalProperty() }
  );

  public Calendar() {}

  public Calendar(ObservableList<Appointment> appointments) {
    this.appointments = appointments;
  }

  public Calendar(Appointment... appointments) {
    this.appointments.setAll(appointments);
  }

  public FilteredList<Appointment> getAppointmentsFor(LocalDate date) {
    return getAppointmentsBetween(
      new TimeInterval(
        LocalDateTime.of(date, LocalTime.MIN),
        LocalDateTime.of(date, LocalTime.MAX)
      )
    );
  }

  public FilteredList<Appointment> getAppointmentsBetween(
    TimeInterval interval
  ) {
    return new FilteredList<Appointment>(
      appointments,
      appointment -> appointment.intervalProperty().get().overlaps(interval)
    );
  }

  public List<Appointment> getAppointments() {
    if (appointments != null && !appointments.isEmpty()) {
      List<Appointment> appointmentsList = new ArrayList<>(appointments.size());
      for (Appointment appointment : appointments) {
        appointmentsList.add(appointment);
      }
      return appointmentsList;
    }
    return new ArrayList<>();
  }

  @Override
  public Appointment get(int index) {
    return appointments.get(index);
  }

  @Override
  public int size() {
    return appointments.size();
  }

  @Override
  protected void doAdd(int index, Appointment element) {
    appointments.add(index, element);
  }

  @Override
  protected Appointment doSet(int index, Appointment element) {
    return appointments.set(index, element);
  }

  @Override
  protected Appointment doRemove(int index) {
    return appointments.remove(index);
  }
}
