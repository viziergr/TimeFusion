package com.timefusion.ui.calendar.rendering;

import com.timefusion.ui.calendar.Appointment;
import com.timefusion.ui.calendar.util.PercentPane;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.layout.Region;

public class FlexAppointmentFactory implements AbstractAppointmentFactory {

  @Override
  public Region createAppointment(Appointment appointment) {
    return AppointmentRenderer.createAppointment(appointment);
  }

  @Override
  public void layoutAppointments(Map<Appointment, Region> guiElements) {
    List<Appointment> sortedAppointments = guiElements
      .keySet()
      .stream()
      .sorted((o1, o2) -> {
        int ending = -(int) Duration
          .between(o1.endTimeProperty(), o2.endTimeProperty())
          .toMinutes();
        if (ending != 0) return ending;
        int start = -(int) Duration
          .between(o1.startTimeProperty(), o2.startTimeProperty())
          .toMinutes();
        return start;
      })
      .collect(Collectors.toList());

    List<Integer> columnIndex = new ArrayList<>(sortedAppointments.size());
    List<Integer> columnsParallel = new ArrayList<>(sortedAppointments.size());

    for (int i = 0; i < sortedAppointments.size(); i++) {
      Appointment app = sortedAppointments.get(i);
      Region reg = guiElements.get(app);
      if (reg != null) {
        columnIndex.add(i, 0);
        columnsParallel.add(i, 1);

        boolean newColumn = true;
        for (int a = 0; a < i; a++) {
          if (
            sortedAppointments
              .get(a)
              .intervalProperty()
              .get()
              .overlaps(app.intervalProperty().get())
          ) {
            if (columnIndex.get(a) > columnIndex.get(i)) {
              columnsParallel.set(
                i,
                Math.max(columnsParallel.get(a), columnsParallel.get(i))
              );
              newColumn = false;
            } else {
              columnIndex.set(
                i,
                Math.max(columnIndex.get(a) + 1, columnIndex.get(i))
              );
              if (newColumn) {
                columnsParallel.set(
                  i,
                  Math.max(columnsParallel.get(a) + 1, columnsParallel.get(i))
                );
                columnsParallel.set(a, columnsParallel.get(a) + 1);
              }
            }
          }
        }
      }
    }

    int i = 0;
    for (Appointment app : sortedAppointments) {
      Region reg = guiElements.get(app);
      if (reg != null) {
        int colIndex = columnIndex.get(i);
        int colParallel = columnsParallel.get(i);
        double left = (double) colIndex / colParallel;
        double right = 1.0 - (double) (colIndex + 1) / colParallel;

        PercentPane.setLeftAnchor(reg, left);
        PercentPane.setRightAnchor(reg, right);
      }
      i++;
    }
  }
}
