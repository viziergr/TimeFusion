package com.timefusion.ui.calendar;

import com.timefusion.ui.calendar.components.DayPane;
import com.timefusion.ui.calendar.components.TimeAxis;
import com.timefusion.ui.calendar.components.TimeIndicator;
import com.timefusion.ui.calendar.controlers.AddEventDialogController;
import com.timefusion.ui.calendar.rendering.AbstractAppointmentFactory;
import com.timefusion.ui.calendar.rendering.FlexAppointmentFactory;
import com.timefusion.ui.calendar.rendering.WeekViewRenderer;
import com.timefusion.ui.calendar.util.TimeInterval;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class WeekView extends CalendarView {

  private static final int dayPaneMinWidth = 50;
  protected LocalTime dayStartTime;
  protected LocalTime dayEndTime;

  protected int numberOfDays;
  protected Pane weekHeaderContainer;
  protected GridPane dayHeadersContainer;
  protected GridPane dayPanesContainer;
  protected Pane timeAxisContainer;
  protected WeekViewRenderer renderer;
  protected AbstractAppointmentFactory appointmentFactory;

  public WeekView() {
    this(
      LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()),
      new Calendar()
    );
  }

  public WeekView(LocalDate dateBegin, Calendar... calendar) {
    this(new SimpleObjectProperty<>(dateBegin), calendar);
  }

  public WeekView(ObjectProperty<LocalDate> dateBegin, Calendar... calendar) {
    this(dateBegin, 7, calendar);
  }

  public WeekView(
    ObjectProperty<LocalDate> dateBegin,
    int numberOfDays,
    Calendar... calendar
  ) {
    this(
      dateBegin,
      numberOfDays,
      LocalTime.MIN,
      LocalTime.MAX,
      new WeekViewRenderer(),
      new FlexAppointmentFactory(),
      calendar
    );
  }

  public WeekView(
    ObjectProperty<LocalDate> dateBegin,
    int numberOfDays,
    LocalTime dayStartTime,
    LocalTime dayEndTime,
    WeekViewRenderer renderer,
    AbstractAppointmentFactory appointmentFactory,
    Calendar... calendar
  ) {
    this.getStylesheets()
      .add(
        getClass()
          .getResource("/com/timefusion/ui/calendar/resources/css/WeekView.css")
          .toExternalForm()
      );

    this.dateProperty = dateBegin;
    this.numberOfDays = numberOfDays;
    this.dayStartTime = dayStartTime;
    this.dayEndTime = dayEndTime;
    this.renderer = renderer;
    this.appointmentFactory = appointmentFactory;
    for (int i = 0; i < calendar.length; i++) {
      this.getCalendars().add(calendar[i]);
    }

    setLayout();
    setContent();

    getDate()
      .addListener(observable -> {
        setContent();
      });

    getCalendars()
      .addListener(
        (InvalidationListener) observable -> {
          setContent();
        }
      );
  }

  private void setLayout() {
    final RowConstraints headerRow = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      USE_PREF_SIZE
    );
    final RowConstraints allDayRow = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      USE_PREF_SIZE
    );
    final RowConstraints calendarRow = new RowConstraints(
      150,
      500,
      Double.POSITIVE_INFINITY,
      Priority.ALWAYS,
      VPos.TOP,
      true
    );
    final ColumnConstraints columnConstraints = new ColumnConstraints(
      400,
      600,
      Double.POSITIVE_INFINITY,
      Priority.SOMETIMES,
      HPos.LEFT,
      true
    );
    this.getRowConstraints().addAll(headerRow, allDayRow, calendarRow);
    this.getColumnConstraints().add(columnConstraints);
    this.getStyleClass().add("weekview");

    Pane weekHeaderContainer = new StackPane();
    weekHeaderContainer.getStyleClass().add("weekview-header-container");
    this.weekHeaderContainer = weekHeaderContainer;

    final ScrollPane scrollPane = new ScrollPane();
    scrollPane.getStyleClass().add("weekview-scrollpane");
    scrollPane.setStyle("-fx-background-color:transparent;");
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    final GridPane scrollPaneContent = new GridPane();
    scrollPaneContent.getStyleClass().add("weekview-scrollpane-content");
    final ColumnConstraints timeColumn = new ColumnConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      USE_PREF_SIZE,
      Priority.ALWAYS,
      HPos.LEFT,
      false
    );
    final ColumnConstraints calendarColumn = new ColumnConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      Double.POSITIVE_INFINITY,
      Priority.ALWAYS,
      HPos.LEFT,
      true
    );
    final RowConstraints rowConstraint = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      Double.POSITIVE_INFINITY,
      Priority.ALWAYS,
      VPos.TOP,
      true
    );
    scrollPaneContent.getColumnConstraints().addAll(timeColumn, calendarColumn);
    scrollPaneContent.getRowConstraints().addAll(rowConstraint);
    scrollPane.setContent(scrollPaneContent);

    Pane timeAxisContainer = new StackPane();
    timeAxisContainer.getStyleClass().add("weekview-timeaxis-container");
    scrollPaneContent.add(timeAxisContainer, 0, 0);
    this.timeAxisContainer = timeAxisContainer;

    final GridPane dayPaneContainer = new GridPane();
    dayPaneContainer.getStyleClass().add("weekview-daypane-container");
    final GridPane dayHeaderContainer = new GridPane();
    dayHeaderContainer.getStyleClass().add("weekview-day-header-container");
    for (int i = 0; i < numberOfDays; i++) {
      final ColumnConstraints appointmentsColumn = new ColumnConstraints(
        USE_PREF_SIZE,
        dayPaneMinWidth,
        Double.POSITIVE_INFINITY,
        Priority.ALWAYS,
        HPos.CENTER,
        true
      );
      dayPaneContainer.getColumnConstraints().add(appointmentsColumn);
      dayHeaderContainer.getColumnConstraints().add(appointmentsColumn);
    }
    final RowConstraints singleDayHeaderRow = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      USE_PREF_SIZE
    );
    final RowConstraints singleDayAppointmentsRow = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      USE_PREF_SIZE
    );
    dayHeaderContainer
      .getRowConstraints()
      .addAll(singleDayHeaderRow, singleDayAppointmentsRow);
    timeAxisContainer
      .widthProperty()
      .addListener(observable -> {
        dayHeaderContainer.setPadding(
          new Insets(0, 17, 0, timeAxisContainer.getWidth() + 1)
        );
      });
    final RowConstraints dayPanesRow = new RowConstraints(
      USE_PREF_SIZE,
      USE_COMPUTED_SIZE,
      Double.POSITIVE_INFINITY,
      Priority.ALWAYS,
      VPos.TOP,
      true
    );
    dayPaneContainer.getRowConstraints().add(dayPanesRow);
    this.dayPanesContainer = dayPaneContainer;
    this.dayHeadersContainer = dayHeaderContainer;
    scrollPaneContent.add(dayPaneContainer, 1, 0);

    this.add(scrollPane, 0, 2);
    this.add(dayHeaderContainer, 0, 1);
    this.add(weekHeaderContainer, 0, 0);
  }

  private void setContent() {
    this.weekHeaderContainer.getChildren().clear();
    this.timeAxisContainer.getChildren().clear();
    this.dayHeadersContainer.getChildren().clear();
    this.dayPanesContainer.getChildren().clear();

    this.timeAxisContainer.getChildren()
      .add(new TimeAxis(dayStartTime, dayEndTime, Duration.ofHours(1)));
    this.weekHeaderContainer.getChildren().add(renderer.createHeaderPane(this));

    for (int i = 0; i < numberOfDays; i++) {
      final LocalDate currentDate = dateProperty.get().plusDays(i);

      final List<FilteredList<Appointment>> appointmentsCurrentDate = calendars
        .stream()
        .map(calendar -> calendar.getAppointmentsFor(currentDate))
        .collect(Collectors.toList());

      final Node dayHeader = renderer.createSingleDayHeader(currentDate);
      dayHeadersContainer.add(dayHeader, i, 0);

      final Node allDay = renderer.createAllDayPane(
        appointmentsCurrentDate
          .stream()
          .flatMap(appointments -> appointments.stream())
          .filter(appointment ->
            appointment
              .intervalProperty()
              .get()
              .overlaps(
                new TimeInterval(
                  LocalDateTime.of(currentDate, LocalTime.MIN),
                  LocalDateTime.of(currentDate, LocalTime.MAX)
                )
              )
          )
          .filter(appointment -> appointment.isFullDayProperty().get())
          .collect(Collectors.toList())
      );
      dayHeadersContainer.add(allDay, i, 1);

      final Node dayBackground = renderer.createDayBackground(currentDate);
      dayPanesContainer.add(dayBackground, i, 0);

      final DayPane dp = new DayPane(
        currentDate,
        dayStartTime,
        dayEndTime,
        appointmentFactory
      );
      final TimeIndicator indicator = new TimeIndicator(dp);
      dayPanesContainer.add(indicator, i, 0);
      appointmentsCurrentDate
        .stream()
        .flatMap(appointments -> appointments.stream())
        .forEach(a -> dp.addAppointment(a));

      appointmentsCurrentDate.forEach(appointments ->
        appointments.addListener(
          (ListChangeListener<Appointment>) c -> {
            while (c.next()) {
              for (Appointment a : c.getAddedSubList()) {
                dp.addAppointment(a);
              }
              for (Appointment a : c.getRemoved()) {
                dp.removeAppointment(a);
              }
            }
          }
        )
      );
    }
  }

  public LocalDate getStartDate() {
    return dateProperty.get();
  }

  public LocalDate getEndDate() {
    return dateProperty.get().plusDays(numberOfDays - 1);
  }

  public void update() {
    this.setContent();
  }

  private void handleAddEventButtonClick() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass()
          .getResource(
            "/com/timefusion/ui/calendar/resources/fxml/AddEventDialog.fxml"
          )
      );
      VBox dialog = loader.load();
      AddEventDialogController controller = loader.getController();

      Scene scene = new Scene(dialog);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
