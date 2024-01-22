package com.timefusion.sync;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.timefusion.dao.EventParticipantDao;
import com.timefusion.localStorage.Entities.EventNature;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.localStorage.Entities.UserEntity;
import com.timefusion.localStorage.JsonUtils;
import com.timefusion.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SyncUtil {

  /**
   * Retrieves the local user ID.
   *
   * @return The local user ID.
   */
  public static int getLocalUserId() {
    UserEntity userEntity = UserEntity.getuserEntityFromJson();
    return userEntity.getId();
  }

  /**
   * Retrieves a list of non-negative event IDs from the local online events.
   *
   * @return A list of non-negative event IDs.
   */
  public static List<Integer> getLocalOnlineEventsIds() {
    List<Integer> nonNegativeIds = new ArrayList<>();

    for (int i = 0; i < EventsEntity.getAllEventEntities().size(); i++) {
      JsonObject eventObject = EventsEntity
        .getAllEventEntities()
        .get(i)
        .getAsJsonObject();
      int eventId = eventObject.get("id").getAsInt();

      if (eventId >= 0) {
        nonNegativeIds.add(eventId);
      }
    }

    return nonNegativeIds;
  }

  /**
   * Retrieves a list of negative event IDs from the local offline events.
   *
   * @return A list of negative event IDs.
   */
  public static List<Integer> getLocalOfflineEventsIds() {
    List<Integer> negativeIds = new ArrayList<>();

    for (int i = 0; i < EventsEntity.getAllEventEntities().size(); i++) {
      JsonObject eventObject = EventsEntity
        .getAllEventEntities()
        .get(i)
        .getAsJsonObject();
      int eventId = eventObject.get("id").getAsInt();

      if (eventId < 0) {
        negativeIds.add(eventId);
      }
    }

    return negativeIds;
  }

  /**
   * Retrieves the IDs of all local events.
   *
   * @return a list of integers representing the IDs of all local events
   */
  public static List<Integer> getLocalEventsIds() {
    List<Integer> allIds = new ArrayList<>();

    for (int i = 0; i < EventsEntity.getAllEventEntities().size(); i++) {
      JsonObject eventObject = EventsEntity
        .getAllEventEntities()
        .get(i)
        .getAsJsonObject();
      int eventId = eventObject.get("id").getAsInt();
      allIds.add(eventId);
    }

    return allIds;
  }

  /**
   * Retrieves the list of remote event IDs associated with the local user.
   *
   * @param databaseUtil the DatabaseUtil object used for executing the SQL query
   * @return a List of Integer representing the remote event IDs
   */
  public static List<Integer> getRemoteEventsIds(DatabaseUtil databaseUtil) {
    try {
      List<Integer> listIds = new ArrayList<>();
      String query =
        "SELECT event_id as id FROM " +
        EventParticipantDao.TABLE_NAME +
        " WHERE participant_id = " +
        getLocalUserId() +
        " UNION " +
        "SELECT id FROM event WHERE creator_id = " +
        getLocalUserId() +
        ";";
      List<Map<String, Object>> mapIds = databaseUtil.executeQuery(query);
      for (Map<String, Object> mapId : mapIds) {
        listIds.add((Integer) mapId.get("id"));
      }
      return listIds;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * Retrieves a list of remote event IDs that are not present in the local storage.
   *
   * @param databaseUtil the database utility object used to interact with the local storage
   * @return a list of remote event IDs that are not present in the local storage
   */
  public static List<Integer> getRemoteIdsNotInLocalStorage(
    DatabaseUtil databaseUtil
  ) {
    List<Integer> DBEventIds = getRemoteEventsIds(databaseUtil);
    DBEventIds.removeAll(getLocalOnlineEventsIds());
    return DBEventIds;
  }

  /**
   * Retrieves a list of local online ICDs (Integer IDs) that are not present in the remote database.
   *
   * @param databaseUtil the DatabaseUtil object used for database operations
   * @return a List of Integer IDs representing the local online ICDs not found in the remote database
   */
  public static List<Integer> getLocalOnlineIcdsNotInRemote(
    DatabaseUtil databaseUtil
  ) {
    List<Integer> localOnlineIds = getLocalOnlineEventsIds();
    localOnlineIds.removeAll(getRemoteEventsIds(databaseUtil));
    return localOnlineIds;
  }

  /**
   * Retrieves a list of local offline event IDs that are not present in the remote database.
   *
   * @param databaseUtil the database utility object used to interact with the remote database
   * @return a list of local offline event IDs not found in the remote database
   */
  public static List<Integer> getLocalOfflineIdsNotInRemote(
    DatabaseUtil databaseUtil
  ) {
    List<Integer> localOfflineIds = getLocalOfflineEventsIds();
    localOfflineIds.removeAll(getRemoteEventsIds(databaseUtil));
    return localOfflineIds;
  }

  /**
   * Retrieves a list of normal events that are not present in the local database.
   *
   * @param databaseUtil the DatabaseUtil object used for executing queries
   * @return a List of Integer representing the IDs of the normal events not in the local database
   */
  public static List<Integer> getNormalEventsNotInLocal(
    DatabaseUtil databaseUtil
  ) {
    try {
      List<Integer> listIds = new ArrayList<>();
      String query =
        "SELECT id FROM event WHERE creator_id = " + getLocalUserId() + ";";
      List<Map<String, Object>> mapIds = databaseUtil.executeQuery(query);
      for (Map<String, Object> mapId : mapIds) {
        listIds.add((Integer) mapId.get("id"));
      }
      listIds.removeAll(getLocalOnlineEventsIds());
      return listIds;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * Retrieves a list of event IDs for events in which the local user has been invited.
   *
   * @param databaseUtil the DatabaseUtil object used for executing the SQL query
   * @return a List of Integer representing the event IDs
   */
  public static List<Integer> getRemoteInvitedEvents(
    DatabaseUtil databaseUtil
  ) {
    try {
      List<Integer> listIds = new ArrayList<>();
      String query =
        "SELECT event_id as id FROM " +
        EventParticipantDao.TABLE_NAME +
        " WHERE participant_id = " +
        getLocalUserId() +
        ";";
      List<Map<String, Object>> mapIds = databaseUtil.executeQuery(query);
      for (Map<String, Object> mapId : mapIds) {
        listIds.add((Integer) mapId.get("event_id"));
      }
      return listIds;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * Retrieves the list of remote invited event IDs that are not present in the local database.
   *
   * @param databaseUtil The DatabaseUtil object used to interact with the database.
   * @return The list of remote invited event IDs not found in the local database.
   */
  public static List<Integer> getRemoteInvitedEventsNotInLocal(
    DatabaseUtil databaseUtil
  ) {
    List<Integer> DBEventIds = getRemoteInvitedEvents(databaseUtil);
    DBEventIds.removeAll(getLocalOnlineEventsIds());
    return DBEventIds;
  }

  /**
   * Retrieves a list of local online event IDs that are not present in the remote database.
   *
   * @param databaseUtil the DatabaseUtil object used to interact with the remote database
   * @return a list of local online event IDs that are not present in the remote database
   */
  public static List<Integer> getLocalOnlineIdsNotInRemote(
    DatabaseUtil databaseUtil
  ) {
    List<Integer> localOnlineIds = getLocalOnlineEventsIds();
    localOnlineIds.removeAll(getRemoteEventsIds(databaseUtil));
    return localOnlineIds;
  }

  /**
   * Retrieves a list of offline events.
   *
   * @return The list of offline events.
   */
  public static List<EventsEntity> getOfflineEvents() {
    List<EventsEntity> offlineEvents = new ArrayList<>();
    List<Integer> negativeIds = SyncUtil.getLocalOfflineEventsIds();
    if (negativeIds.size() > 0) {
      for (int i = 0; i < negativeIds.size(); i++) {
        offlineEvents.add(EventsEntity.getEventEntityById(negativeIds.get(i)));
      }
    }
    return offlineEvents;
  }

  /**
   * Retrieves the IDs of locally deleted events from the events JSON.
   *
   * @return A list of integers representing the IDs of locally deleted events.
   */
  public static List<Integer> getLocalDeletedEventsIds() {
    JsonElement eventsJsonElement = JsonUtils.readJsonPart(
      EventsEntity.EVENTS_ENTITY_NAME
    );
    List<Integer> deletedEvents = new ArrayList<>();

    if (eventsJsonElement.isJsonArray()) {
      JsonArray eventsJsonArray = eventsJsonElement.getAsJsonArray();
      for (JsonElement eventElement : eventsJsonArray) {
        JsonObject eventObject = eventElement.getAsJsonObject();
        if (
          eventObject
            .get("nature")
            .getAsString()
            .equals(EventNature.DELETED.toString())
        ) {
          deletedEvents.add(eventObject.get("id").getAsInt());
        }
      }
    }
    return deletedEvents;
  }

  /**
   * Retrieves the IDs of the events that were added while the application was offline.
   *
   * @return A list of integers representing the IDs of the added events.
   */
  public static List<Integer> getOfflineAddedEventsIds() {
    List<Integer> localOfflineIds = getLocalOfflineEventsIds();
    List<Integer> localOfflineAddedIds = new ArrayList<>();
    for (int i = 0; i < localOfflineIds.size(); i++) {
      if (
        EventsEntity
          .getEventEntityById(localOfflineIds.get(i))
          .getNature()
          .equals(EventNature.ADDED)
      ) {
        System.out.println("added");
        localOfflineAddedIds.add(localOfflineIds.get(i));
      }
    }
    return localOfflineAddedIds;
  }

  /**
   * Retrieves the list of local event IDs that are marked as denied.
   *
   * @return The list of local event IDs marked as denied.
   */
  public static List<Integer> getLocalDeniedEventsIds() {
    List<Integer> localOnlineEventsIds = getLocalOnlineEventsIds();
    List<Integer> localDeniedEventsIds = new ArrayList<>();
    for (int i = 0; i < localOnlineEventsIds.size(); i++) {
      if (
        EventsEntity
          .getEventEntityById(localOnlineEventsIds.get(i))
          .getNature()
          .equals(EventNature.DENIED)
      ) {
        localDeniedEventsIds.add(localOnlineEventsIds.get(i));
      }
    }
    return localDeniedEventsIds;
  }
}
