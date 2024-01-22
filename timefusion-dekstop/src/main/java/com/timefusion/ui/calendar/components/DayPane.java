package com.timefusion.ui.calendar.components;

import com.timefusion.ui.calendar.Appointment;
import com.timefusion.ui.calendar.rendering.AbstractAppointmentFactory;
import com.timefusion.ui.calendar.rendering.FlexAppointmentFactory;
import com.timefusion.ui.calendar.util.PercentPane;
import com.timefusion.ui.calendar.util.TimeInterval;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.*;
import javafx.scene.layout.Region;

/**
 * A DayPane is a Pane that displays all appointments for a given day.
 * The DayPane is associated with a date and displays all appointments for this date.
 * The DayPane covers a time interval between a start and an end time.
 * Appointments that are outside of this interval are not displayed.
 * The DayPane uses a custom renderer for displaying appointments.
 */
public class DayPane extends PercentPane {

  private final LocalDate dayDate;
  private final ObjectProperty<LocalTime> dayStartTimeProperty;
  private final ObjectProperty<LocalTime> dayEndTimeProperty;
  private final Map<Appointment, Region> appointments;
  private final AbstractAppointmentFactory renderer;

  /**
   * Creates a new DayPane with associated with the given date. The DayPane covers the full day and
   * uses the default renderer.
   *
   * @param date The date associated with this DayPane. Only appointments for this date will be displayed.
   */
  public DayPane(LocalDate date) {
    this(date, LocalTime.MIN, LocalTime.MAX);
  }

  /**
   * Creates a new DayPane associated with the given date. The DayPane covers the interval between
   * dayStartTime and dayEndTime. Only appointments within this interval at this date are displayed.
   *
   * @param date          The date associated with this DayPane.
   * @param dayStartTime  The start value of the time interval being displayed
   * @param dayEndTime    The end value of the time interval being displayed.
   */
  public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
    this(date, dayStartTime, dayEndTime, new FlexAppointmentFactory());
  }

  /**
   * Creates a new DayPane associated with the given date. The DayPane covers the interval between
   * dayStartTime and dayEndTime. Only appointments within this interval at this date are displayed.
   * Additionally a custom renderer can be specified which takes care of the visual appearance of
   * Appointments.
   *
   * @param date          The date associated with this DayPane.
   * @param dayStartTime  The start value of the time interval being displayed.
   * @param dayEndTime    The end value of the time interval being displayed.
   * @param renderer      A custom renderer that is used for displaying appointments.
   */
  public DayPane(
    LocalDate date,
    LocalTime dayStartTime,
    LocalTime dayEndTime,
    AbstractAppointmentFactory renderer
  ) {
    this.dayDate = date;
    this.dayStartTimeProperty = new SimpleObjectProperty<>(dayStartTime);
    this.dayEndTimeProperty = new SimpleObjectProperty<>(dayEndTime);
    this.renderer = renderer;

    this.setPrefHeight(
        Duration.between(dayStartTime, dayEndTime).toMinutes() / 2
      );

    this.appointments = new HashMap<>();

    this.dayStartTimeProperty()
      .addListener(observable -> {
        appointments.keySet().forEach(this::addGuiElement);
        renderer.layoutAppointments(appointments);
      });
    this.dayEndTimeProperty()
      .addListener(observable -> {
        appointments.keySet().forEach(this::addGuiElement);
        renderer.layoutAppointments(appointments);
      });
  }

  /**
   * Adds a new appointment to the DayPane.
   * Calculates the correct position and layout and changes them if the appointment's interval
   * is updated.
   *
   * @param appointment The appointment that should be added to the DayPane.
   */
  public void addAppointment(Appointment appointment) {
    Region p = addGuiElement(appointment);
    appointments.put(appointment, p);
    renderer.layoutAppointments(appointments);

    appointment
      .intervalProperty()
      .addListener(observable -> {
        appointments.put(appointment, addGuiElement(appointment));
        renderer.layoutAppointments(appointments);
      });
  }

  /**
   * Removes an appointment from the DayView so that it will not be rendered.
   *
   * @param appointment Appointment to be removed.
   */
  public void removeAppointment(Appointment appointment) {
    Region region = appointments.get(appointment);
    appointments.remove(appointment);
    if (region != null) {
      this.getChildren().remove(region);
      renderer.layoutAppointments(appointments);
    }
  }

  /**
   * Adds a new appointment to the DayPane.
   * Calculates the correct position and layout and changes them if the appointment's interval
   * is updated.
   *
   * @param appointment The appointment that should be added to the DayPane.
   * @return The Region that is used to display the appointment.
   */
  protected Region addGuiElement(Appointment appointment) {
    Region region = appointments.getOrDefault(appointment, null);

    if (
      appointment
        .intervalProperty()
        .get()
        .overlaps(
          new TimeInterval(
            LocalDateTime.of(getDayDate(), dayStartTimeProperty().get()),
            LocalDateTime.of(getDayDate(), dayEndTimeProperty().get())
          )
        )
    ) {
      if (region != null) {
        this.getChildren().remove(region);
      }
      region = renderer.createAppointment(appointment);

      long minutesPerDay = Duration
        .between(dayStartTimeProperty().get(), dayEndTimeProperty().get())
        .toMinutes();

      if (
        appointment
          .intervalProperty()
          .get()
          .startsBefore(
            LocalDateTime.of(getDayDate(), dayStartTimeProperty().get())
          )
      ) {
        PercentPane.setTopAnchor(region, 0.0);
      } else {
        PercentPane.setTopAnchor(
          region,
          (double) TimeInterval
            .between(
              dayStartTimeProperty().get(),
              appointment.startTimeProperty()
            )
            .getDuration()
            .abs()
            .toMinutes() /
          minutesPerDay
        );
      }
      if (
        appointment
          .intervalProperty()
          .get()
          .endsAfter(LocalDateTime.of(getDayDate(), dayEndTimeProperty().get()))
      ) {
        PercentPane.setBottomAnchor(region, 0.0);
      } else {
        PercentPane.setBottomAnchor(
          region,
          (double) TimeInterval
            .between(dayEndTimeProperty().get(), appointment.endTimeProperty())
            .getDuration()
            .abs()
            .toMinutes() /
          minutesPerDay
        );
      }

      PercentPane.setLeftAnchor(region, 0.0);
      PercentPane.setRightAnchor(region, 0.0);
      this.getChildren().add(region);
      return region;
    } else {
      if (region != null) {
        this.getChildren().remove(region);
      }
      return null;
    }
  }

  /**
   * Returns the date the DayPane is associated with.
   * @return  The associated date.
   */
  public LocalDate getDayDate() {
    return dayDate;
  }

  /**
   * Returns the start value of the displayed interval.
   * @return The start value of the displayed interval.
   */
  public ObjectProperty<LocalTime> dayStartTimeProperty() {
    return dayStartTimeProperty;
  }

  /**
   * Returns the end value of the displayed interval.
   * @return The end value of the displayed interval.
   */
  public ObjectProperty<LocalTime> dayEndTimeProperty() {
    return dayEndTimeProperty;
  }
}
