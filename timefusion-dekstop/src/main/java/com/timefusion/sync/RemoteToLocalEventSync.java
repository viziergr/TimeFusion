package com.timefusion.sync;

import com.timefusion.dao.EventDao;
import com.timefusion.dao.EventParticipantDao;
import com.timefusion.dao.UserDao;
import com.timefusion.localStorage.Entities.EventNature;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.localStorage.Entities.InformationEntity;
import com.timefusion.model.Event;
import com.timefusion.model.User;
import com.timefusion.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RemoteToLocalEventSync {

  private static void addNormalEventsToLocal(DatabaseUtil databaseUtil) {
    List<Integer> normalEventsNotInLocal = SyncUtil.getNormalEventsNotInLocal(
      databaseUtil
    );
    if (!normalEventsNotInLocal.isEmpty()) {
      String query =
        "SELECT * FROM " +
        EventDao.TABLE_NAME +
        " WHERE id IN (" +
        normalEventsNotInLocal.toString().replace("[", "").replace("]", "") +
        ")";
      try {
        List<Map<String, Object>> mapEvents = databaseUtil.executeQuery(query);
        for (Map<String, Object> mapEvent : mapEvents) {
          Event event = EventDao.mapResultSetToEvent(mapEvent);
          EventsEntity eventsEntity = new EventsEntity(
            event,
            EventNature.UNCHANGED,
            getParticipantsToNormalEvent(databaseUtil, event.getId())
          );
          eventsEntity.addEventEntity();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private static List<User> getParticipantsToNormalEvent(
    DatabaseUtil databaseUtil,
    int eventId
  ) {
    List<User> DBUsers = new ArrayList<>();

    try {
      String participantsQuery =
        "SELECT * FROM " +
        UserDao.TABLE_NAME +
        " WHERE id IN (SELECT participant_id FROM " +
        EventParticipantDao.TABLE_NAME +
        " WHERE event_id = " +
        eventId +
        " AND participant_id != " +
        SyncUtil.getLocalUserId() +
        ")";
      List<Map<String, Object>> mapUsers = databaseUtil.executeQuery(
        participantsQuery
      );
      if (mapUsers.isEmpty()) {
        return DBUsers;
      }
      for (Map<String, Object> mapUser : mapUsers) {
        User user = UserDao.mapResultSetToUser(
          Collections.singletonList(mapUser)
        );
        DBUsers.add(user);
      }

      return DBUsers;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  private static void addInvitedEventsToLocal(DatabaseUtil databaseUtil) {
    List<Integer> invitedEventsNotInLocal = SyncUtil.getRemoteInvitedEventsNotInLocal(
      databaseUtil
    );
    if (!invitedEventsNotInLocal.isEmpty()) {
      String query =
        "SELECT * FROM " +
        EventDao.TABLE_NAME +
        " WHERE id IN (" +
        invitedEventsNotInLocal.toString().replace("[", "").replace("]", "") +
        ")";
      try {
        List<Map<String, Object>> mapEvents = databaseUtil.executeQuery(query);
        for (Map<String, Object> mapEvent : mapEvents) {
          Event event = EventDao.mapResultSetToEvent(mapEvent);
          EventsEntity eventsEntity = new EventsEntity(
            event,
            EventNature.INVITED,
            getParticipantsToInvitedEvent(databaseUtil, event.getId())
          );
          eventsEntity.addEventEntity();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private static List<User> getParticipantsToInvitedEvent(
    DatabaseUtil databaseUtil,
    int eventId
  ) {
    List<User> DBUsers = new ArrayList<>();
    DBUsers.add(getCreatorOfEvent(databaseUtil, eventId));

    try {
      String participantsQuery =
        "SELECT * FROM " +
        UserDao.TABLE_NAME +
        " WHERE id IN (SELECT participant_id FROM " +
        EventParticipantDao.TABLE_NAME +
        " WHERE event_id = " +
        eventId +
        " AND participant_id != " +
        SyncUtil.getLocalUserId() +
        ")";
      List<Map<String, Object>> mapUsers = databaseUtil.executeQuery(
        participantsQuery
      );
      if (mapUsers.isEmpty()) {
        return DBUsers;
      }
      for (Map<String, Object> mapUser : mapUsers) {
        User user = UserDao.mapResultSetToUser(
          Collections.singletonList(mapUser)
        );
        DBUsers.add(user);
      }

      return DBUsers;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  private static User getCreatorOfEvent(
    DatabaseUtil databaseUtil,
    int eventId
  ) {
    try {
      String creatorQuery =
        "SELECT * FROM " +
        UserDao.TABLE_NAME +
        " WHERE id IN (SELECT creator_id FROM " +
        EventDao.TABLE_NAME +
        " WHERE id = " +
        eventId +
        ")";
      List<Map<String, Object>> mapCreator = databaseUtil.executeQuery(
        creatorQuery
      );
      for (Map<String, Object> mapUser : mapCreator) {
        User user = UserDao.mapResultSetToUser(
          Collections.singletonList(mapUser)
        );
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void deleteStaleLocalOnlineEvents(DatabaseUtil databaseUtil) {
    List<Integer> localOnlineIdsNotInDB = SyncUtil.getLocalOnlineIdsNotInRemote(
      databaseUtil
    );
    if (!localOnlineIdsNotInDB.isEmpty()) {
      for (Integer id : localOnlineIdsNotInDB) {
        EventsEntity.deleteEventEntity(id);
      }
    }
  }

  public static void addOrUpdateInformationEntity(
    boolean updateLastUpdated,
    boolean updateLastSynced
  ) {
    InformationEntity informationEntity = InformationEntity.getInformationEntity();
    if (!updateLastSynced && !updateLastUpdated) {
      return;
    }
    if (updateLastUpdated) {
      informationEntity.setLastUpdatedNow();
    }

    if (updateLastSynced) {
      informationEntity.setLastSyncedNow();
    }

    informationEntity.updateInformationEntity();
  }

  public static void synchronizeEvents(DatabaseUtil databaseUtil) {
    deleteStaleLocalOnlineEvents(databaseUtil);
    addNormalEventsToLocal(databaseUtil);
    addInvitedEventsToLocal(databaseUtil);
  }
}
