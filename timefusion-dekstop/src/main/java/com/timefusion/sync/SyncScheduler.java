package com.timefusion.sync;

import com.timefusion.dao.EventDao;
import com.timefusion.localStorage.Entities.InformationEntity;
import com.timefusion.ui.calendar.DisplaySync;
import com.timefusion.util.DatabaseUtil;
import demo.Main;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SyncScheduler {

  private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
    1
  );
  private static final int INITIAL_DELAY_SECONDS = 0;
  private static final int SCHEDULE_INTERVAL_SECONDS = 10;

  public static void main(String[] args) {
    try {
      EventDao eventDao = new EventDao();
      DatabaseUtil databaseUtil = new DatabaseUtil();
      InformationEntity informationEntity = new InformationEntity();
      scheduleSync(eventDao, databaseUtil, informationEntity);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Launch the JavaFX application
    Main.main(args);

    Runtime
      .getRuntime()
      .addShutdownHook(
        new Thread(() -> {
          scheduler.shutdown();
          try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
              scheduler.shutdownNow();
            }
          } catch (InterruptedException e) {
            scheduler.shutdownNow();
          }
        })
      );
  }

  public static void scheduleSync(
    EventDao eventDao,
    DatabaseUtil databaseUtil,
    InformationEntity informationEntity
  ) {
    scheduler.scheduleAtFixedRate(
      () -> {
        try {
          System.out.println("Syncing...");
          NetworkStateManager.detectWifiState();
          if (NetworkStateManager.hasWifiConnection()) {
            informationEntity.setLastSyncedNow();
            LocalToRemoteEventSync.synchronize(eventDao);
            RemoteToLocalEventSync.synchronizeEvents(databaseUtil);
          }
          System.out.println(NetworkStateManager.hasWifiConnection());
          informationEntity.setLastUpdatedNow();
          informationEntity.updateInformationEntity();
          DisplaySync.synchronizeDisplay();
        } catch (Exception e) {
          e.printStackTrace();
        }
      },
      INITIAL_DELAY_SECONDS,
      SCHEDULE_INTERVAL_SECONDS,
      TimeUnit.SECONDS
    );
  }
}
