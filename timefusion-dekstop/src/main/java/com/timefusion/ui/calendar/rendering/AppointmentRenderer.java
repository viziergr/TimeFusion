package com.timefusion.ui.calendar.rendering;

import com.timefusion.localStorage.Entities.EventNature;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.localStorage.Entities.ParticipantsEntity;
import com.timefusion.ui.calendar.Appointment;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class for creating the nodes to an appointment.
 * This class is used by {@link FlexAppointmentFactory} and {@link StackingAppointmentFactory}.
 */
public class AppointmentRenderer {

  public static Region createAppointment(Appointment appointment) {
    if (appointment.isFullDayProperty().get()) {
      Pane p = new Pane();
      p
        .getStylesheets()
        .add(
          AppointmentRenderer.class.getResource(
              "/com/timefusion/ui/calendar/resources/css/Appointment.css"
            )
            .toString()
        );
      p.getStyleClass().add("appointment-fullday");
      return p;
    } else {
      BorderPane p = new BorderPane();
      p
        .getStylesheets()
        .add(
          AppointmentRenderer.class.getResource(
              "/com/timefusion/ui/calendar/resources/css/Appointment.css"
            )
            .toExternalForm()
        );
      if (appointment.isOffline()) {
        p.getStyleClass().add("appointment-offline"); //appointment
      } else if (appointment.isInvited()) {
        p.getStyleClass().add("appointment-invited");
      } else if (appointment.isPrivate()) {
        p.getStyleClass().add("appointment-private");
      } else {
        p.getStyleClass().add("appointment");
      }
      VBox content = new VBox();

      Label starttime = new Label(
        DateTimeFormatter
          .ofLocalizedTime(FormatStyle.SHORT)
          .format(appointment.startTimeProperty())
      );
      starttime.getStyleClass().add("lblStartTime");
      content.getChildren().add(starttime);
      Label title = new Label(appointment.titleProperty().get());
      content.getChildren().add(title);

      Pane leftBar = new Pane();
      leftBar.getStyleClass().add("leftbar");
      leftBar.setPrefWidth(5.0);

      p.setOnMouseClicked(event -> handleAppointmentClick(appointment));

      p.setLeft(leftBar);
      p.setCenter(content);
      p.setMargin(content, new Insets(0.0, 5.0, 0.0, 5.0));
      return p;
    }
  }

  private static void handleAppointmentClick(Appointment appointment) {
    if (appointment.isOffline()) {
      showOfflineAppointmentDetails(appointment);
    } else if (appointment.isInvited()) {
      showInvitedAppointmentDetails(appointment);
    } else {
      showAppointmentDetailsStage(appointment);
    }
  }

  private static void showAppointmentDetailsStage(Appointment appointment) {
    Stage detailsStage = new Stage();
    detailsStage.initModality(Modality.APPLICATION_MODAL);
    detailsStage.setTitle("Appointment Details");

    Label titleLabel = new Label("Title: " + appointment.titleProperty().get());
    Label startTimeLabel = new Label(
      "Start Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.startTimeProperty())
    );
    Label endTimeLabel = new Label(
      "End Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.endTimeProperty())
    );
    Label descriptionLabel = new Label(
      "Description: " + appointment.getEventEntity().getDescription()
    );
    Label locationLabel = new Label(
      "Location: " + appointment.getEventEntity().getLocation()
    );

    VBox detailsVBox = new VBox(
      titleLabel,
      startTimeLabel,
      endTimeLabel,
      descriptionLabel,
      locationLabel
    );
    detailsVBox.setAlignment(Pos.CENTER);
    detailsVBox.setSpacing(10);
    detailsVBox.setPadding(new Insets(10));

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event ->
      handleDeleteButtonAction(appointment, detailsStage)
    );

    detailsVBox.getChildren().add(deleteButton);

    Scene detailsScene = new Scene(detailsVBox, 400, 250);
    detailsStage.setScene(detailsScene);

    detailsStage.show();
  }

  private static void showOfflineAppointmentDetails(Appointment appointment) {
    Stage detailsStage = new Stage();
    detailsStage.initModality(Modality.APPLICATION_MODAL);
    detailsStage.setTitle("Offline Appointment Details");

    Label titleLabel = new Label("Title: " + appointment.titleProperty().get());
    Label startTimeLabel = new Label(
      "Start Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.startTimeProperty())
    );
    Label endTimeLabel = new Label(
      "End Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.endTimeProperty())
    );
    Label descriptionLabel = new Label(
      "Description: " + appointment.getEventEntity().getDescription()
    );
    Label locationLabel = new Label(
      "Location: " + appointment.getEventEntity().getLocation()
    );

    Label offlineLabel = new Label("This event is offline.");

    VBox detailsVBox = new VBox(
      titleLabel,
      startTimeLabel,
      endTimeLabel,
      descriptionLabel,
      locationLabel,
      offlineLabel
    );
    detailsVBox.setAlignment(Pos.CENTER);
    detailsVBox.setSpacing(10);
    detailsVBox.setPadding(new Insets(10));

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event ->
      handleDeleteButtonAction(appointment, detailsStage)
    );

    detailsVBox.getChildren().add(deleteButton);

    Scene detailsScene = new Scene(detailsVBox, 400, 300);
    detailsStage.setScene(detailsScene);

    detailsStage.show();
  }

  private static void showInvitedAppointmentDetails(Appointment appointment) {
    Stage detailsStage = new Stage();
    detailsStage.initModality(Modality.APPLICATION_MODAL);
    detailsStage.setTitle("Invited Appointment Details");

    Label titleLabel = new Label("Title: " + appointment.titleProperty().get());
    Label startTimeLabel = new Label(
      "Start Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.startTimeProperty())
    );
    Label endTimeLabel = new Label(
      "End Time: " +
      DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .format(appointment.endTimeProperty())
    );
    Label descriptionLabel = new Label(
      "Description: " + appointment.getEventEntity().getDescription()
    );
    Label locationLabel = new Label(
      "Location: " + appointment.getEventEntity().getLocation()
    );

    VBox detailsVBox = new VBox(
      titleLabel,
      startTimeLabel,
      endTimeLabel,
      descriptionLabel,
      locationLabel
    );

    // Display participants if available
    ParticipantsEntity[] participants = appointment
      .getEventEntity()
      .getParticipants();
    if (participants.length > 0) {
      Label participantsLabel = new Label("Participants:");
      VBox participantsVBox = new VBox(participantsLabel);
      int i = 1;
      for (ParticipantsEntity participant : participants) {
        Label participantLabel = new Label(
          "" +
          i +
          ": " +
          participant.getFirstName() +
          " " +
          participant.getLastName() +
          "\n"
        );
        participantsVBox.setAlignment(Pos.CENTER);
        participantsVBox.getChildren().add(participantLabel);
        i++;
      }

      detailsVBox.getChildren().add(participantsVBox);
    }

    // Indicate if the event is private
    if (appointment.getEventEntity().getIsPrivate()) {
      Label privateLabel = new Label("This event is private.");
      detailsVBox.getChildren().add(privateLabel);
    }

    detailsVBox.setAlignment(Pos.CENTER);
    detailsVBox.setSpacing(10);
    detailsVBox.setPadding(new Insets(10));

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event ->
      handleDeleteButtonAction(appointment, detailsStage)
    );

    detailsVBox.getChildren().add(deleteButton);

    Scene detailsScene = new Scene(detailsVBox, 400, 300);
    detailsStage.setScene(detailsScene);

    detailsStage.show();
  }

  private static void handleDeleteButtonAction(
    Appointment appointment,
    Stage detailsStage
  ) {
    EventsEntity.deleteEventEntity(appointment.getEventEntity().getId());
    if (!appointment.isOffline() && !appointment.isInvited()) {
      appointment.getEventEntity().setNature(EventNature.DELETED);
      appointment.getEventEntity().addEventEntity();
    } else if (appointment.isInvited()) {
      appointment.getEventEntity().setNature(EventNature.DENIED);
      appointment.getEventEntity().addEventEntity();
    }
    detailsStage.close();
  }
}
