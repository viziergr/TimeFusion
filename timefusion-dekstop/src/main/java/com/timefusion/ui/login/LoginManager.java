package com.timefusion.ui.login;

import com.timefusion.ui.login.controlers.LoginControler;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginManager {

  public static boolean performLogin() {
    return true;
  }

  public static boolean showLoginDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(
        LoginManager.class.getResource(
            "/com/timefusion/ui/login/resources/fxml/log-in.fxml"
          )
      );
      Parent dialog = loader.load();

      Scene scene = new Scene(dialog);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.initModality(Modality.APPLICATION_MODAL);

      LoginControler loginController = loader.getController();

      stage.showAndWait();

      if (loginController.isLoginSuccessful()) {
        return true;
      } else {
        showLoginFailedAlert();
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private static void showLoginFailedAlert() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Login Failed");
    alert.setHeaderText(null);
    alert.setContentText("Invalid username or password. Please try again.");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {}
  }
}
