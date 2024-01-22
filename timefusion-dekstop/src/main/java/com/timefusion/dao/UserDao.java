package com.timefusion.dao;

import com.timefusion.model.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao extends GenericDao<User> {

  public static final String TABLE_NAME = "user";
  private final Map<String, Class<?>> schema = new HashMap<>();

  public UserDao() throws SQLException {
    super(TABLE_NAME);
    defineSchema();
  }

  /**
   * Retrieves the value of a specific column from the User object.
   *
   * @param columnName the name of the column to retrieve the value from
   * @param user the User object from which to retrieve the value
   * @return the value of the specified column from the User object, or null if the column name is invalid
   */
  private Object getColumnValue(String columnName, User user) {
    switch (columnName) {
      case "id":
        return user.getId();
      case "first_name":
        return user.getFirstName();
      case "last_name":
        return user.getLastName();
      case "email":
        return user.getEmail();
      case "password":
        return user.getPassword();
      default:
        return null;
    }
  }

  /**
   * Finds a user by their email address.
   *
   * @param email the email address of the user to find
   * @return the User object if found, or null if not found
   */
  public User findByEmail(String email) {
    String query =
      "SELECT id, email, password, first_name, last_name FROM " +
      TABLE_NAME +
      " WHERE email = ?";

    try {
      List<Object> params = Collections.singletonList(email);
      List<Map<String, Object>> result = super.databaseUtil.executeQuery(
        query,
        params
      );

      if (!result.isEmpty()) {
        return mapResultSetToUser(result);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Maps the result set to a User object.
   *
   * @param result the result set to be mapped
   * @return the User object
   */
  public static User mapResultSetToUser(List<Map<String, Object>> result) {
    if (result.isEmpty()) {
      return null;
    } else if (result.size() > 1) {
      throw new IllegalArgumentException(
        "Expected a single result, got " + result.size() + " instead."
      );
    }

    User user = new User(
      (int) result.get(0).get("id"),
      (String) result.get(0).get("first_name"),
      (String) result.get(0).get("last_name"),
      (String) result.get(0).get("email"),
      (String) result.get(0).get("password")
    );
    return user;
  }

  /**
   * Maps a User object to a map of column values.
   *
   * @param user The User object to be mapped.
   * @return A map of column names and their corresponding values.
   */
  private Map<String, Object> mapUserToColumnValues(User user) {
    Map<String, Object> columnValues = new HashMap<>();

    for (String columnName : schema.keySet()) {
      columnValues.put(columnName, getColumnValue(columnName, user));
    }

    return columnValues;
  }

  /**
   * Inserts a record into the database for the given user.
   *
   * @param user The user object to be inserted.
   * @return The id of the inserted record.
   * @throws SQLException If an error occurs while inserting the record.
   */
  public int insertUserRecord(User user) throws SQLException {
    return super.databaseUtil.insertRecord(
      tableName,
      this.mapUserToColumnValues(user)
    );
  }

  /**
   * Updates a record in the database for a given User entity by its ID.
   *
   * @param user The User entity to be updated.
   * @return The number of rows affected by the update operation.
   * @throws SQLException If an error occurs while updating the record.
   */
  public int updateUserRecord(User user) throws SQLException {
    Map<String, Object> columnValues = mapUserToColumnValues(user);
    columnValues.remove("id");
    return super.databaseUtil.updateRecordById(
      tableName,
      "id",
      user.getId(),
      columnValues
    );
  }

  /**
   * Deletes a record from the database table based on the provided user object.
   *
   * @param user The user object containing the record to be deleted.
   * @return The number of rows affected by the deletion operation.
   * @throws SQLException If an error occurs while deleting the record.
   */
  public int deleteUserRecord(User user) throws SQLException {
    return super.databaseUtil.deleteRecordById(tableName, "id", user.getId());
  }

  /**
   * Retrieves user records based on the provided criteria.
   *
   * @param criteriaMap a map containing the criteria for retrieving user records
   * @return a list of maps representing the retrieved user records
   * @throws SQLException if there is an error while retrieving the records from the database
   */
  public List<User> retrieveUsersRecords(Map<String, Object> criteriaMap)
    throws SQLException {
    return this.mapUserSetToUsers(
        super.databaseUtil.retrieveRecords(tableName, criteriaMap)
      );
  }

  /**
   * Maps a list of result set rows to a list of User objects.
   *
   * @param resultSet the result set containing the user data
   * @return a list of User objects
   */
  private List<User> mapUserSetToUsers(List<Map<String, Object>> resultSet) {
    List<User> users = new ArrayList<>();

    for (Map<String, Object> row : resultSet) {
      User user = new User(
        (int) row.get("id"),
        (String) row.get("first_name"),
        (String) row.get("last_name"),
        (String) row.get("email"),
        (String) row.get("password")
      );
      users.add(user);
    }

    return users;
  }

  @Override
  protected void defineSchema() {
    try {
      schema.put("id", int.class);
      schema.put("first_name", String.class);
      schema.put("last_name", String.class);
      schema.put("email", String.class);
      schema.put("password", String.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected int insertRecord(User entity) throws SQLException {
    return (insertUserRecord(entity));
  }

  @Override
  protected int updateRecordByEntity(User entity) throws SQLException {
    return (updateUserRecord(entity));
  }

  @Override
  protected List<User> retrieveRecordsWithCriteria(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    return retrieveUsersRecords(criteriaMap);
  }
}
