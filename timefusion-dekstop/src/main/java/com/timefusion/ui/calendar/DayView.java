package com.timefusion.ui.calendar;

import com.timefusion.ui.calendar.rendering.AbstractAppointmentFactory;
import com.timefusion.ui.calendar.rendering.DayViewRenderer;
import com.timefusion.ui.calendar.rendering.FlexAppointmentFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DayView extends WeekView {

  public DayView() {
    this(LocalDate.now(), new Calendar());
  }

  public DayView(LocalDate date, Calendar... calendar) {
    this(new SimpleObjectProperty<>(date), calendar);
  }

  public DayView(ObjectProperty<LocalDate> date, Calendar... calendar) {
    this(
      date,
      LocalTime.MIN,
      LocalTime.MAX,
      new DayViewRenderer(),
      new FlexAppointmentFactory(),
      calendar
    );
  }

  public DayView(
    ObjectProperty<LocalDate> date,
    LocalTime dayStartTime,
    LocalTime dayEndTime,
    DayViewRenderer renderer,
    AbstractAppointmentFactory appointmentFactory,
    Calendar... calendar
  ) {
    super(
      date,
      1,
      dayStartTime,
      dayEndTime,
      renderer,
      appointmentFactory,
      calendar
    );
  }
}
