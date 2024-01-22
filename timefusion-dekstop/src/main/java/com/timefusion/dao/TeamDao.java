package com.timefusion.dao;

import com.timefusion.model.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The TeamDao class is responsible for performing database operations related to the Team entity.
 * It extends the GenericDao class and provides specific implementations for inserting, updating, and deleting Team records.
 */
public class TeamDao extends GenericDao<Team> {

  private static final String TABLE_NAME = "team";
  private final Map<String, Class<?>> schema = new HashMap<>();

  public TeamDao() throws SQLException {
    super(TABLE_NAME);
    defineSchema();
  }

  private Object getColumnValue(String columnName, Team team) {
    switch (columnName) {
      case "id":
        return team.getId();
      case "name":
        return team.getName();
      case "description":
        return team.getDescription();
      case "color":
        return team.getColor();
      default:
        return null;
    }
  }

  private Team mapResultSetToTeam(List<Map<String, Object>> result) {
    if (result.isEmpty()) {
      return null;
    } else if (result.size() > 1) {
      throw new IllegalArgumentException(
        "More than one team found with the given id"
      );
    }
    Team team = new Team(
      (int) result.get(0).get("id"),
      (String) result.get(0).get("name"),
      (String) result.get(0).get("description"),
      (String) result.get(0).get("color")
    );

    return team;
  }

  private Map<String, Object> mapTeamToColumnValues(Team team) {
    Map<String, Object> columnValues = new HashMap<>();
    for (String columnName : schema.keySet()) {
      columnValues.put(columnName, getColumnValue(columnName, team));
    }

    return columnValues;
  }

  public int insertTeamRecord(Team team) throws SQLException {
    return super.databaseUtil.insertRecord(
      tableName,
      this.mapTeamToColumnValues(team)
    );
  }

  public int updateTeamRecord(Team team) throws SQLException {
    Map<String, Object> columnValues = this.mapTeamToColumnValues(team);
    return super.databaseUtil.updateRecordById(
      tableName,
      "id",
      team.getId(),
      columnValues
    );
  }

  public int deleteTeamRecord(Team team) throws SQLException {
    return super.databaseUtil.deleteRecordById(tableName, "id", team.getId());
  }

  public List<Team> retrieveTeamsRecords(Map<String, Object> criteriaMap)
    throws SQLException {
    return this.mapTeamSetToTeams(
        super.databaseUtil.retrieveRecords(tableName, criteriaMap)
      );
  }

  private List<Team> mapTeamSetToTeams(List<Map<String, Object>> resultSet) {
    List<Team> teams = new ArrayList<>();
    for (Map<String, Object> result : resultSet) {
      Team team = new Team(
        (int) result.get("id"),
        (String) result.get("name"),
        (String) result.get("description"),
        (String) result.get("color")
      );
      teams.add(team);
    }

    return teams;
  }

  @Override
  protected void defineSchema() {
    try {
      schema.put("id", Long.class);
      schema.put("name", String.class);
      schema.put("description", String.class);
      schema.put("color", String.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected int insertRecord(Team team) throws SQLException {
    return this.insertTeamRecord(team);
  }

  @Override
  protected int updateRecordByEntity(Team team) throws SQLException {
    return this.updateTeamRecord(team);
  }

  @Override
  protected List<Team> retrieveRecordsWithCriteria(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return this.retrieveTeamsRecords(criteriaMap);
  }
}
