package com.timefusion.ui.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DayChooser extends VBox {

  protected ObjectProperty<LocalDate> selectedDateProperty;
  protected ObjectProperty<Month> displayedMonth;
  protected int displayedYear;

  public DayChooser(ObjectProperty<LocalDate> selectedDate) {
    this.getStylesheets()
      .add(
        getClass()
          .getResource(
            "/com/timefusion/ui/calendar/resources/css/DayChooser.css"
          )
          .toExternalForm()
      );
    this.selectedDateProperty = selectedDate;
    this.displayedMonth =
      new SimpleObjectProperty<>(selectedDateProperty.get().getMonth());
    this.displayedYear = selectedDateProperty.get().getYear();

    final BorderPane header = new BorderPane();
    final Button btnMonthLeft = new Button("<");
    final Button btnMonthRight = new Button(">");
    header.setLeft(btnMonthLeft);
    header.setRight(btnMonthRight);
    final Label lblMonth = new Label(selectedDate.get().getMonth().name());
    lblMonth.getStyleClass().add("daychooser-label-month");

    lblMonth.setOnMouseClicked(event -> {
      displayedYear = selectedDateProperty.get().getYear();
      displayedMonth.set(selectedDateProperty.get().getMonth());
    });
    header.setCenter(lblMonth);
    this.getChildren().add(header);

    final GridPane monthPane = new GridPane();
    monthPane.getStyleClass().add("daychooser-monthgrid");
    VBox.setVgrow(monthPane, Priority.ALWAYS);
    populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
    this.getChildren().add(monthPane);

    selectedDateProperty.addListener(observable -> {
      displayedMonth.set(selectedDateProperty.get().getMonth());
      displayedYear = selectedDateProperty.get().getYear();
      populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
      lblMonth.setText(displayedMonth.get().name());
    });

    displayedMonth.addListener(observable -> {
      lblMonth.setText(displayedMonth.get().name());
      populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
    });

    btnMonthLeft.setOnAction(event -> {
      if (displayedMonth.get() == Month.JANUARY) {
        displayedYear--;
      }
      displayedMonth.set(displayedMonth.get().minus(1));
    });
    btnMonthRight.setOnAction(event -> {
      if (displayedMonth.get() == Month.DECEMBER) {
        displayedYear++;
      }
      displayedMonth.set(displayedMonth.get().plus(1));
    });
  }

  private void populateMonthPane(GridPane monthPane, Month month, int year) {
    monthPane.getChildren().clear();

    int currentRow = 0;
    for (
      LocalDate d = LocalDate.of(year, month, 1);
      d.getMonthValue() == month.getValue();
      d = d.plusDays(1)
    ) {
      Node dayNode = renderDayItem(d);
      final LocalDate currentDate = d;
      dayNode.setOnMouseClicked(event -> selectedDateProperty.set(currentDate));

      int column = d.getDayOfWeek().getValue();
      monthPane.add(dayNode, column, currentRow);

      if (column == 7) {
        currentRow++;
      }
    }
  }

  /**
   * Creates a node for a given day. Styling should be done via CSS, but
   * appropriate CSS classes are assigned to each node.
   * @param date A given date the node is associated with.
   * @return A node that displays the day of month.
   */
  private Node renderDayItem(LocalDate date) {
    Label lblDate = new Label("" + date.getDayOfMonth());
    lblDate.getStyleClass().add("daychooser-day");
    if (
      date.getDayOfWeek() == DayOfWeek.SATURDAY ||
      date.getDayOfWeek() == DayOfWeek.SUNDAY
    ) {
      lblDate.getStyleClass().add("daychooser-weekend");
    } else {
      lblDate.getStyleClass().add("daychooser-weekday");
    }
    if (date.equals(selectedDateProperty.get())) {
      lblDate.getStyleClass().add("daychooser-selected-day");
    }
    if (date.equals(LocalDate.now())) {
      lblDate.getStyleClass().add("daychooser-current-day");
    }

    return lblDate;
  }

  public ObjectProperty<LocalDate> selectedDateProperty() {
    return selectedDateProperty;
  }
}
