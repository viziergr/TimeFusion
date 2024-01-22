package com.timefusion.dao;

import com.timefusion.model.Event;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EventDao class is responsible for performing database operations related to the Event entity.
 * It extends the GenericDao class and provides specific implementations for inserting, updating, and deleting Event records.
 */
public class EventDao extends GenericDao<Event> {

  public static final String TABLE_NAME = "event";
  private final Map<String, Class<?>> schema = new HashMap<>();

  public EventDao() throws SQLException {
    super(TABLE_NAME);
    defineSchema();
  }

  private Object getColumnValue(String columnName, Event event) {
    switch (columnName) {
      case "id":
        return event.getId();
      case "title":
        return event.getTitle();
      case "start_time":
        return event.getStartTime();
      case "end_time":
        return event.getEndTime();
      case "location":
        return event.getLocation();
      case "description":
        return event.getDescription();
      case "is_private":
        return event.getIsPrivate();
      case "creator_id":
        return event.getCreatorId();
      default:
        return null;
    }
  }

  public static Event mapResultSetToEvent(Map<String, Object> result) {
    if (result.isEmpty()) {
      return null;
    }
    Event event = new Event(
      (int) result.get("id"),
      (String) result.get("title"),
      ((LocalDateTime) result.get("start_time")),
      ((LocalDateTime) result.get("end_time")),
      (String) result.get("location"),
      (String) result.get("description"),
      (boolean) result.get("is_private"),
      (int) result.get("creator_id")
    );
    return event;
  }

  private Map<String, Object> mapEventToColumnValues(Event event) {
    Map<String, Object> columnValues = new HashMap<>();

    for (String columnName : schema.keySet()) {
      columnValues.put(columnName, getColumnValue(columnName, event));
    }

    return columnValues;
  }

  public int insertEventRecord(Event event) throws SQLException {
    return super.databaseUtil.insertRecord(
      tableName,
      this.mapEventToColumnValues(event)
    );
  }

  public int updateEventRecord(Event event) throws SQLException {
    Map<String, Object> columnValues = mapEventToColumnValues(event);
    columnValues.remove("id");
    return super.databaseUtil.updateRecordById(
      tableName,
      "id",
      event.getId(),
      columnValues
    );
  }

  public int deleteEventRecord(Event event) throws SQLException {
    return super.databaseUtil.deleteRecordById(tableName, "id", event.getId());
  }

  public List<Event> retrieveEventsRecords(Map<String, Object> criteriaMap)
    throws SQLException {
    return this.mapEventToColumnValues(
        super.databaseUtil.retrieveRecords(tableName, criteriaMap)
      );
  }

  private List<Event> mapEventToColumnValues(List<Map<String, Object>> result) {
    List<Event> events = new ArrayList<>();

    for (Map<String, Object> row : result) {
      events.add(mapResultSetToEvent(row));
    }

    return events;
  }

  @Override
  protected void defineSchema() {
    try {
      schema.put("id", int.class);
      schema.put("title", String.class);
      schema.put("start_time", LocalDateTime.class);
      schema.put("end_time", LocalDateTime.class);
      schema.put("location", String.class);
      schema.put("description", String.class);
      schema.put("is_private", boolean.class);
      schema.put("creator_id", int.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected int insertRecord(Event event) throws SQLException {
    return this.insertEventRecord(event);
  }

  @Override
  protected int updateRecordByEntity(Event event) throws SQLException {
    return this.updateEventRecord(event);
  }

  @Override
  protected List<Event> retrieveRecordsWithCriteria(
    String tablename,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return this.retrieveEventsRecords(criteriaMap);
  }
}
