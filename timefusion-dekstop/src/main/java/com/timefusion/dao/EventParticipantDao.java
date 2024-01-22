package com.timefusion.dao;

import com.timefusion.model.EventParticipant;
import com.timefusion.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventParticipantDao extends GenericDao<EventParticipant> {

  public static final String TABLE_NAME = "event_participant";
  private final Map<String, Class<?>> schema = new HashMap<>();

  public EventParticipantDao() throws SQLException {
    super(TABLE_NAME);
    defineSchema();
  }

  public DatabaseUtil getDatabaseUtil() {
    return super.databaseUtil;
  }

  private Object getColumnValue(
    String columnName,
    EventParticipant eventparticipant
  ) {
    switch (columnName) {
      case "id":
        return eventparticipant.getId();
      case "participant_id":
        return eventparticipant.getParticipantId();
      case "event_id":
        return eventparticipant.getEventId();
      default:
        return null;
    }
  }

  public static EventParticipant mapResultSetToEventParticipant(
    Map<String, Object> resultSet
  ) {
    if (resultSet.isEmpty()) {
      return null;
    }
    return new EventParticipant(
      (Integer) resultSet.get("id"),
      (Integer) resultSet.get("participant_id"),
      (Integer) resultSet.get("event_id")
    );
  }

  private Map<String, Object> mapEventparticipantToColumnValues(
    EventParticipant eventParticipant
  ) {
    Map<String, Object> columnValues = new HashMap<>();
    for (String columnName : schema.keySet()) {
      columnValues.put(
        columnName,
        getColumnValue(columnName, eventParticipant)
      );
    }
    return columnValues;
  }

  public int insertEventParticipantRecord(EventParticipant eventParticipant)
    throws SQLException {
    return super.databaseUtil.insertRecord(
      tableName,
      this.mapEventparticipantToColumnValues(eventParticipant)
    );
  }

  public int updateEventParticipantRecord(EventParticipant eventParticipant)
    throws SQLException {
    Map<String, Object> columnValues =
      this.mapEventparticipantToColumnValues(eventParticipant);
    return super.databaseUtil.updateRecordById(
      tableName,
      "id",
      eventParticipant.getId(),
      columnValues
    );
  }

  public int deleteEventParticipantRecord(EventParticipant eventParticipant)
    throws SQLException {
    return super.databaseUtil.deleteRecordById(
      tableName,
      "id",
      eventParticipant.getId()
    );
  }

  public List<EventParticipant> retrieveEventParticipantRecords(
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return this.mapEventparticipantSetToEventparticipants(
        super.databaseUtil.retrieveRecords(tableName, criteriaMap)
      );
  }

  private List<EventParticipant> mapEventparticipantSetToEventparticipants(
    List<Map<String, Object>> resultSet
  ) {
    List<EventParticipant> eventparticipants = new ArrayList<>();
    for (Map<String, Object> result : resultSet) {
      EventParticipant eventparticipant = new EventParticipant(
        (Integer) result.get("id"),
        (Integer) result.get("participant_id"),
        (Integer) result.get("event_id")
      );
      eventparticipants.add(eventparticipant);
    }
    return eventparticipants;
  }

  public void deleteEventParticipantRecordByEventId(int eventParticipantId)
    throws SQLException {
    super.databaseUtil.deleteRecordById(tableName, "id", eventParticipantId);
  }

  @Override
  protected void defineSchema() {
    try {
      schema.put("id", Integer.class);
      schema.put("participant_id", Integer.class);
      schema.put("event_id", Integer.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected int insertRecord(EventParticipant eventParticipant)
    throws SQLException {
    return this.insertEventParticipantRecord(eventParticipant);
  }

  @Override
  protected int updateRecordByEntity(EventParticipant eventParticipant)
    throws SQLException {
    return this.updateEventParticipantRecord(eventParticipant);
  }

  @Override
  protected List<EventParticipant> retrieveRecordsWithCriteria(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return this.retrieveEventParticipantRecords(criteriaMap);
  }
}
