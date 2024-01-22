module com.timefusion {
  requires transitive javafx.controls;
  requires transitive javafx.fxml;
  requires transitive javafx.graphics;
  requires transitive javafx.base;
  requires transitive java.sql;
  requires transitive java.desktop;
  requires transitive jbcrypt;
  requires transitive de.jensd.fx.glyphs.fontawesome;
  requires transitive org.controlsfx.controls;
  requires transitive com.google.gson;

  opens com.timefusion to javafx.fxml;
  opens com.timefusion.ui.calendar to javafx.fxml;
  opens com.timefusion.ui.calendar.rendering to javafx.fxml;
  opens com.timefusion.ui.calendar.controlers to javafx.fxml;
  opens com.timefusion.ui.calendar.components to javafx.fxml;
  opens com.timefusion.ui.calendar.util to javafx.fxml;
  opens com.timefusion.util to java.sql;
  opens com.timefusion.localStorage.Entities to com.google.gson;
  opens com.timefusion.localStorage to com.google.gson;
  opens com.launcher to javafx.fxml;
  opens com.timefusion.sync to javafx.fxml;
  opens com.timefusion.ui.login to javafx.fxml;
  opens com.timefusion.ui.login.controlers to javafx.fxml;

  exports com.timefusion.config ;
  exports com.timefusion.dao ;
  exports com.timefusion.exception ;
  exports com.timefusion.model ;
  exports com.timefusion.service ;
  exports com.timefusion.util ;
  exports com.launcher ;
  exports com.timefusion.ui.calendar ;
  exports com.timefusion.ui.calendar.components ;
  exports com.timefusion.ui.calendar.controlers ;
  exports com.timefusion.ui.calendar.rendering ;
  exports com.timefusion.ui.calendar.util ;
  exports com.timefusion.ui.login ;
  exports com.timefusion.ui.login.controlers ;
  exports com.timefusion ;
  exports com.timefusion.sync to javafx.graphics;
  exports com.timefusion.localStorage.Entities to com.google.gson;
}
