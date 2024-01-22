package com.timefusion.ui.calendar.components;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class TimeAxis extends GridPane {

  private final ObjectProperty<LocalTime> timeStartProperty;
  private final ObjectProperty<LocalTime> timeEndProperty;
  private final ObjectProperty<Duration> timeStepsProperty;
  private final boolean horizontal;
  private DateTimeFormatter formatter;

  public TimeAxis(LocalTime timeStart, LocalTime timeEnd, Duration timeSteps) {
    this(
      new SimpleObjectProperty<>(timeStart),
      new SimpleObjectProperty<>(timeEnd),
      new SimpleObjectProperty<>(timeSteps)
    );
  }

  public TimeAxis(
    ObjectProperty<LocalTime> timeStart,
    ObjectProperty<LocalTime> timeEnd,
    ObjectProperty<Duration> timeSteps
  ) {
    this(timeStart, timeEnd, timeSteps, DateTimeFormatter.ofPattern("HH:mm"));
  }

  public TimeAxis(
    LocalTime timeStart,
    LocalTime timeEnd,
    Duration timeSteps,
    DateTimeFormatter formatter
  ) {
    this(
      new SimpleObjectProperty<>(timeStart),
      new SimpleObjectProperty<>(timeEnd),
      new SimpleObjectProperty<>(timeSteps),
      formatter
    );
  }

  public TimeAxis(
    ObjectProperty<LocalTime> timeStart,
    ObjectProperty<LocalTime> timeEnd,
    ObjectProperty<Duration> timeSteps,
    DateTimeFormatter formatter
  ) {
    this.timeStartProperty = timeStart;
    this.timeEndProperty = timeEnd;
    this.timeStepsProperty = timeSteps;
    this.formatter = formatter;
    this.horizontal = false;

    this.getStyleClass().add("time-axis");

    getTimeStartProperty().addListener(observable -> createLabels());
    getTimeEndProperty().addListener(observable -> createLabels());
    getTimeStepsProperty().addListener(observable -> createLabels());

    createLabels();
  }

  private void createLabels() {
    this.getChildren().clear();
    this.getRowConstraints().clear();
    this.getColumnConstraints().clear();

    for (
      LocalTime currentTime = getTimeStartProperty().get();
      currentTime.isBefore(getTimeEndProperty().get());
    ) {
      Label lblTime = new Label();
      lblTime.setText(currentTime.format(getFormatter()));
      lblTime.getStyleClass().add("time-axis-label");

      if (horizontal) {
        lblTime
          .widthProperty()
          .addListener(o ->
            lblTime.setTranslateX(-lblTime.widthProperty().getValue() / 2)
          );

        ColumnConstraints column = new ColumnConstraints(
          0,
          USE_COMPUTED_SIZE,
          Double.POSITIVE_INFINITY,
          Priority.SOMETIMES,
          HPos.LEFT,
          true
        );
        this.getColumnConstraints().add(column);
        this.add(lblTime, this.getColumnConstraints().size() - 1, 0);
      } else {
        lblTime
          .heightProperty()
          .addListener(o ->
            lblTime.setTranslateY(-lblTime.heightProperty().getValue() / 2)
          );
        RowConstraints row = new RowConstraints(
          0,
          USE_COMPUTED_SIZE,
          Double.POSITIVE_INFINITY,
          Priority.SOMETIMES,
          VPos.TOP,
          true
        );
        this.getRowConstraints().add(row);
        this.add(lblTime, 0, this.getRowConstraints().size() - 1);
      }

      LocalTime newTime = currentTime.plusMinutes(
        getTimeStepsProperty().get().toMinutes()
      );
      if (newTime.isAfter(currentTime)) {
        currentTime = newTime;
      } else {
        break;
      }
    }
  }

  public ObjectProperty<LocalTime> getTimeStartProperty() {
    return timeStartProperty;
  }

  public ObjectProperty<LocalTime> getTimeEndProperty() {
    return timeEndProperty;
  }

  public ObjectProperty<Duration> getTimeStepsProperty() {
    return timeStepsProperty;
  }

  public DateTimeFormatter getFormatter() {
    return formatter;
  }

  public void setFormatter(DateTimeFormatter formatter) {
    this.formatter = formatter;
    createLabels();
  }
}
