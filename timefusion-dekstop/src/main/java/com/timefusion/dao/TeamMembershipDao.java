package com.timefusion.dao;

import com.timefusion.model.TeamMembership;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMembershipDao extends GenericDao<TeamMembership> {

  private static final String TABLE_NAME = "team_membership";
  private final Map<String, Class<?>> schema = new HashMap<>();

  public TeamMembershipDao() throws SQLException {
    super(TABLE_NAME);
    defineSchema();
  }

  private Object getColumnValue(
    String columnName,
    TeamMembership teamMembership
  ) {
    switch (columnName) {
      case "id":
        return teamMembership.getId();
      case "user_id":
        return teamMembership.getUserId();
      case "team_id":
        return teamMembership.getTeamId();
      case "role":
        return teamMembership.getRole();
      default:
        return null;
    }
  }

  private TeamMembership mapResultSetToTeamMembership(
    Map<String, Object> resultSet
  ) {
    if (resultSet.isEmpty()) {
      return null;
    } else if (resultSet.size() == 1) {
      throw new IllegalArgumentException("Invalid number of results found");
    }
    return new TeamMembership(
      (Integer) resultSet.get("id"),
      (Integer) resultSet.get("user_id"),
      (Integer) resultSet.get("team_id"),
      (String) resultSet.get("role")
    );
  }

  private Map<String, Object> mapTeamMembershipToColumnValues(
    TeamMembership teamMembership
  ) {
    Map<String, Object> columnValues = new HashMap<>();
    for (String columnName : schema.keySet()) {
      columnValues.put(columnName, getColumnValue(columnName, teamMembership));
    }

    return columnValues;
  }

  public int insertTeamMembershipRecord(TeamMembership teamMembership)
    throws SQLException {
    return super.databaseUtil.insertRecord(
      tableName,
      mapTeamMembershipToColumnValues(teamMembership)
    );
  }

  public int updateTeamMembershipRecord(TeamMembership teamMembership)
    throws SQLException {
    Map<String, Object> columnValues = mapTeamMembershipToColumnValues(
      teamMembership
    );
    return super.databaseUtil.updateRecordById(
      tableName,
      "id",
      teamMembership.getId(),
      columnValues
    );
  }

  public int deleteTeamMembershipRecord(TeamMembership teamMembership)
    throws SQLException {
    return super.databaseUtil.deleteRecordById(
      tableName,
      "id",
      teamMembership.getId()
    );
  }

  public List<TeamMembership> getTeamMembershipRecords(
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return this.mapTeamMembershipSetToTeamMemberships(
        super.databaseUtil.retrieveRecords(tableName, criteriaMap)
      );
  }

  private List<TeamMembership> mapTeamMembershipSetToTeamMemberships(
    List<Map<String, Object>> resultSet
  ) {
    List<TeamMembership> teamMemberships = new ArrayList<>();
    for (Map<String, Object> result : resultSet) {
      teamMemberships.add(mapResultSetToTeamMembership(result));
    }
    return teamMemberships;
  }

  @Override
  protected void defineSchema() {
    try {
      schema.put("id", Integer.class);
      schema.put("user_id", Integer.class);
      schema.put("team_id", Integer.class);
      schema.put("role", String.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected int insertRecord(TeamMembership teamMembership)
    throws SQLException {
    return insertTeamMembershipRecord(teamMembership);
  }

  @Override
  protected int updateRecordByEntity(TeamMembership teamMembership)
    throws SQLException {
    return updateTeamMembershipRecord(teamMembership);
  }

  @Override
  protected List<TeamMembership> retrieveRecordsWithCriteria(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return getTeamMembershipRecords(criteriaMap);
  }
}
