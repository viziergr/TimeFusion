package com.timefusion.ui.calendar.controlers;

import com.timefusion.localStorage.Entities.EventNature;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.localStorage.Entities.ParticipantsEntity;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEventDialogController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TextField fromTextField;

  @FXML
  private TextField locationField;

  @FXML
  private TextField titleField;

  @FXML
  private Button publicEventButton;

  @FXML
  private DatePicker dateField;

  @FXML
  private Button privateEventButton;

  @FXML
  private TextField toTextField;

  @FXML
  private TextField descriptionTextField;

  @FXML
  private Button addEventButton;

  private boolean isPrivate;

  /**
   * Sets the location field to the given location
   * @param isPrivate
   */
  public void eventSetPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  /**
   * Returns the location of the event
   * @return boolean isPrivate
   */
  public boolean eventIsPrivate() {
    return isPrivate;
  }

  @FXML
  void mouse(ActionEvent event) {}

  /**
   * Handles the action event when the public event button is clicked.
   * Sets the event as public and updates the UI.
   *
   * @param event the action event triggered by clicking the public event button
   */
  @FXML
  void handlePublicEvent(ActionEvent event) {
    eventSetPrivate(false);
    publicEventButton.setStyle(
      "-fx-background-color: #4C95CE; -fx-background-radius: 15;"
    );
    privateEventButton.setStyle(
      "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;"
    );
  }

  /**
   * Handles the action event when the private event button is clicked.
   * Sets the event as private and updates the button styles accordingly.
   *
   * @param event the action event triggered by clicking the private event button
   */
  @FXML
  void handlePrivateEvent(ActionEvent event) {
    eventSetPrivate(true);
    privateEventButton.setStyle(
      "-fx-background-color: #81C457; -fx-background-radius: 15;"
    );
    publicEventButton.setStyle(
      "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 15;"
    );
  }

  @FXML
  void handleAddEvent(ActionEvent event) {
    String title = titleField.getText();
    String location = locationField.getText();
    String description = descriptionTextField.getText();
    LocalDate date = dateField.getValue();
    String startTime = formatTime(fromTextField.getText());
    String endTime = formatTime(toTextField.getText());

    boolean isPrivate = eventIsPrivate();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = date.format(dateFormatter);

    if (!isStartTimeBeforeEndTime(startTime, endTime)) {
      showErrorAlert(
        "Invalid Time Range",
        "Start time must be before end time.",
        toTextField
      );
    } else {
      LocalDateTime startDateTime = LocalDateTime.parse(
        formattedDate + " " + startTime + ":00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      );

      LocalDateTime endDateTime = LocalDateTime.parse(
        formattedDate + " " + endTime + ":00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      );

      EventsEntity eventsEntity = createEventsEntity(
        title,
        description,
        location,
        startDateTime,
        endDateTime,
        isPrivate
      );
      eventsEntity.addEventEntity();
      Stage stage = (Stage) addEventButton.getScene().getWindow();
      stage.close();
    }
  }

  private String formatTime(String time) {
    return time.length() < 5 ? "0" + time : time;
  }

  private EventsEntity createEventsEntity(
    String title,
    String description,
    String location,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    boolean isPrivate
  ) {
    return new EventsEntity(
      EventNature.ADDED,
      isPrivate,
      title,
      description,
      location,
      startDateTime,
      endDateTime,
      new ParticipantsEntity[] { new ParticipantsEntity() }
    );
  }

  private boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
    LocalTime start = LocalTime.parse(startTime);
    LocalTime end = LocalTime.parse(endTime);
    return start.isBefore(end);
  }

  @FXML
  void initialize() {
    assert fromTextField !=
    null : "fx:id=\"fromTextField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert locationField !=
    null : "fx:id=\"locationField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert titleField !=
    null : "fx:id=\"titleField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert publicEventButton !=
    null : "fx:id=\"publicEventButton\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert dateField !=
    null : "fx:id=\"dateField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert addEventButton !=
    null : "fx:id=\"addEventButton\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert privateEventButton !=
    null : "fx:id=\"privateEventButton\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert toTextField !=
    null : "fx:id=\"toTextField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";
    assert descriptionTextField !=
    null : "fx:id=\"descriptionTextField\" was not injected: check your FXML file 'AddEventDialog.fxml'.";

    addFocusLostListener(fromTextField);
    addFocusLostListener(toTextField);
    addFocusLostListener(titleField);
    addFocusLostListener(locationField);
    addFocusLostListener(descriptionTextField);
    addFocusLostListener(dateField);
  }

  /**
   * Adds a focus lost listener to the given TextField.
   * When the TextField loses focus, it performs validation based on the type of TextField.
   * If the TextField is fromTextField or toTextField, it validates the time format.
   * If the TextField is titleField, it validates the title.
   * If the TextField is locationField, it validates the location.
   * If the TextField is descriptionTextField, it validates the description.
   *
   * @param textField the TextField to add the focus lost listener to
   */
  private void addFocusLostListener(TextField textField) {
    textField
      .focusedProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (!newValue) {
          if (textField == fromTextField || textField == toTextField) {
            validateTimeFormat(textField);
          } else if (textField == titleField) {
            validateTitle(textField);
          } else if (textField == locationField) {
            validateLocation(textField);
          } else if (textField == descriptionTextField) {
            validateDescription(textField);
          }
        }
      });
  }

  private void addFocusLostListener(DatePicker dateField) {
    dateField
      .focusedProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (!newValue) {
          validateDateFormat(dateField);
        }
      });
  }

  /**
   * Validates the time format of the given text field.
   * If the input is not empty and is not in the format HH:mm (e.g., 11:00),
   * an error alert is displayed.
   *
   * @param textField the text field to validate
   */
  private void validateTimeFormat(TextField textField) {
    String input = textField.getText().trim();
    if (!input.isEmpty() && !isValidTimeFormat(input)) {
      showErrorAlert(
        "Invalid Time Format",
        "Please enter time in HH:mm format (e.g., 11:00).",
        textField
      );
    }
  }

  /**
   * Validates the title entered in the text field.
   * If the title exceeds 100 characters, an error alert is displayed.
   *
   * @param textField the text field containing the title
   */
  private void validateTitle(TextField textField) {
    String input = textField.getText().trim();
    if (input.length() > 100) {
      showErrorAlert(
        "Title is Too Long",
        "Title cannot exceed 100 characters.",
        textField
      );
    }
  }

  /**
   * Validates the location input in the text field.
   * If the input exceeds 50 characters, an error alert is displayed.
   *
   * @param textField the text field containing the location input
   */
  private void validateLocation(TextField textField) {
    String input = textField.getText().trim();
    if (input.length() > 50) {
      showErrorAlert(
        "Location is Too Long",
        "Location cannot exceed 50 characters.",
        textField
      );
    }
  }

  /**
   * Validates the description entered in the text field.
   * If the description exceeds 150 characters, an error alert is displayed.
   *
   * @param textField the text field containing the description
   */
  private void validateDescription(TextField textField) {
    String input = textField.getText().trim();
    if (input.length() > 150) {
      showErrorAlert(
        "Description is Too Long",
        "Description cannot exceed 150 characters.",
        textField
      );
    }
  }

  /**
   * Validates the date format entered in the DatePicker field.
   * If the format is invalid, an error alert is displayed.
   *
   * @param dateField The DatePicker field to validate.
   */
  private void validateDateFormat(DatePicker dateField) {
    String input = dateField.getEditor().getText().trim();
    if (!input.isEmpty()) {
      try {
        if (!isValidDateFormat(input)) {
          showErrorAlert(
            "Invalid Date Format",
            "Please enter a valid date.",
            dateField
          );
        }
      } catch (DateTimeParseException e) {
        showErrorAlert(
          "Invalid Date Format",
          "Please enter a valid date.",
          dateField
        );
      }
    }
  }

  /**
   * Checks if the given input string is a valid date format in the format "dd/MM/yyyy".
   *
   * @param input the input string to be checked
   * @return true if the input string is a valid date format and is not in the past, false otherwise
   */
  private boolean isValidDateFormat(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
      LocalDate parsedDate = LocalDate.parse(input, formatter);

      return !LocalDate.now().isAfter(parsedDate);
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  /**
   * Checks if the input string has a valid time format.
   * The valid time format is "HH:mm" where HH is the hour in 24-hour format and mm is the minutes.
   *
   * @param input the string to be checked
   * @return true if the input has a valid time format, false otherwise
   */
  private boolean isValidTimeFormat(String input) {
    return input.matches("\\d{1,2}:\\d{2}");
  }

  /**
   * Displays an error alert with the specified title and content.
   * Clears the provided TextField after displaying the alert.
   *
   * @param title     the title of the error alert
   * @param content   the content of the error alert
   * @param textField the TextField to be cleared after displaying the alert
   */
  private void showErrorAlert(
    String title,
    String content,
    TextField textField
  ) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();

    textField.clear();
  }

  /**
   * Displays an error alert dialog with the specified title and content.
   * Clears the text in the specified DatePicker after displaying the alert.
   *
   * @param title     the title of the error alert dialog
   * @param content   the content of the error alert dialog
   * @param dateField the DatePicker whose text will be cleared after displaying the alert
   */
  private void showErrorAlert(
    String title,
    String content,
    DatePicker dateField
  ) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();

    dateField.getEditor().clear();
  }
}
