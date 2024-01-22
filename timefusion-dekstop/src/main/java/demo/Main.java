package demo;

import com.timefusion.localStorage.JsonUtils;
import com.timefusion.ui.calendar.Calendar;
import com.timefusion.ui.calendar.WeekView;
import com.timefusion.ui.login.LoginManager;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

  private static WeekView weekView;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws InterruptedException {
    if (JsonUtils.isLocalStorageEmpty()) {
      if (LoginManager.showLoginDialog()) {
        initializeMainApplication(primaryStage);
      } else {
        Platform.exit();
        System.exit(0);
      }
    } else {
      initializeMainApplication(primaryStage);
    }
  }

  public static void initializeMainApplication(Stage primaryStage) {
    weekView = new WeekView(LocalDate.now(), new Calendar());
    Image customIcon = new Image(
      Main.class.getResourceAsStream(
          "/com/timefusion/ui/calendar/resources/png/Logo.png"
        )
    );
    Stage stage = new Stage();
    stage.getIcons().add(customIcon);
    stage.setScene(new Scene(weekView));
    stage.setTitle("TimeFusion 1.0");
    stage.setMinWidth(100);
    stage.setMinHeight(100);

    stage.setOnCloseRequest(event -> {
      // Perform cleanup actions here if needed TODO
      Platform.exit();
      System.exit(0);
    });

    stage.show();
    System.out.println("Hello World");
  }

  public static WeekView getWeekView() {
    return weekView;
  }
}
