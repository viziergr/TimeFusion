package com.timefusion.ui.calendar.rendering;

import com.timefusion.ui.calendar.Appointment;
import java.util.Map;
import javafx.scene.layout.Region;

public interface AbstractAppointmentFactory {
  /**
   * Creates a Region representing an appointment.
   *
   * @param appointment the appointment to be represented
   * @return the created Region
   */
  Region createAppointment(Appointment appointment);

  /**
   * Updates the layout of the given appointments.
   *
   * @param guiElements a map containing the appointments and their corresponding Region
   */
  void layoutAppointments(Map<Appointment, Region> guiElements);
}
