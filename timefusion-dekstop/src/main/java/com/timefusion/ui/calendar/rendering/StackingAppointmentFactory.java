package com.timefusion.ui.calendar.rendering;

import com.timefusion.ui.calendar.Appointment;
import com.timefusion.ui.calendar.util.PercentPane;
import com.timefusion.ui.calendar.util.TimeInterval;
import java.time.Duration;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.layout.Region;

public class StackingAppointmentFactory implements AbstractAppointmentFactory {

  @Override
  public Region createAppointment(Appointment appointment) {
    return AppointmentRenderer.createAppointment(appointment);
  }

  @Override
  public void layoutAppointments(Map<Appointment, Region> guiElements) {
    List<Appointment> sortedAppointments = guiElements
      .keySet()
      .stream()
      .sorted((o1, o2) ->
        -(int) Duration
          .between(o1.startTimeProperty(), o2.startTimeProperty())
          .toMinutes()
      )
      .collect(Collectors.toList());

    int numberoffulldayapp = 0;
    ListIterator<Appointment> iterator = sortedAppointments.listIterator();
    for (int index = 0; iterator.hasNext(); index++) {
      Appointment a = iterator.next();
      if (a.isFullDayProperty().get()) {
        if (guiElements.get(a) != null) {
          PercentPane.setLeftAnchor(guiElements.get(a), 0.0);
          guiElements.get(a).setMaxWidth(10.0);
          guiElements.get(a).setMinWidth(10.0);
          numberoffulldayapp++;
        }
      } else {
        final TimeInterval interval = a.intervalProperty().get();
        List<Appointment> stack = sortedAppointments
          .stream()
          .limit(index)
          .filter(appointment ->
            appointment.intervalProperty().get().overlaps(interval)
          )
          .filter(appointment -> !appointment.isFullDayProperty().get())
          .collect(Collectors.toList());

        for (int i = 0; i < stack.size(); i++) {
          if (guiElements.get(stack.get(i)) != null) {
            PercentPane.setLeftAnchor(guiElements.get(stack.get(i)), i * 0.1);
            PercentPane.setRightAnchor(
              guiElements.get(stack.get(i)),
              0.1 * (stack.size() - i)
            );
          }
        }
        if (guiElements.get(a) != null) {
          PercentPane.setRightAnchor(guiElements.get(a), 0.0);
          PercentPane.setLeftAnchor(guiElements.get(a), 0.1 * stack.size());
          guiElements.get(a).toFront();
        }
      }
    }
  }
}
