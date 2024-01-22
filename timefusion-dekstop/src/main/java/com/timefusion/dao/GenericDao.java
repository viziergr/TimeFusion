package com.timefusion.dao;

import com.timefusion.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The GenericDao class is an abstract class that provides a generic implementation for data access objects (DAOs).
 * It defines common methods for interacting with a database table, such as inserting, updating, and deleting records.
 * Subclasses of GenericDao should provide specific implementations for defining the schema, validating the schema,
 * inserting records, updating records, and deleting records.
 *
 * @param <T> The type of the entity for which the DAO is created.
 */
public abstract class GenericDao<T> {

  protected final DatabaseUtil databaseUtil;
  protected final String tableName;

  /**
   * Constructs a new GenericDao object with the specified table name.
   *
   * @param tableName the name of the database table
   * @throws SQLException
   */
  public GenericDao(String tableName) throws SQLException {
    this.databaseUtil = new DatabaseUtil();
    this.tableName = tableName;
  }

  /**
   * Defines the expected schema for a record.
   * Subclasses should implement this method to define the schema for the specific table.
   */
  protected abstract void defineSchema();

  /**
   * Inserts a record into the table.
   * Subclasses should implement this method to insert a record into the specific table.
   *
   * @param entity the entity representing the record to be inserted
   * @return the number of rows affected by the insert operation
   * @throws SQLException if an error occurs while inserting the record
   */
  protected abstract int insertRecord(T entity) throws SQLException;

  /**
   * Updates a record in the table.
   * Subclasses should implement this method to update a record in the specific table.
   *
   * @param entity the entity representing the record to be updated
   * @return the number of rows affected by the update operation
   * @throws SQLException if an error occurs while updating the record
   */
  protected abstract int updateRecordByEntity(T entity) throws SQLException;

  /**
   * Retrieves a record from the specified table based on the given criteria.
   *
   * @param tableName    the name of the table to retrieve the record from
   * @param criteriaMap  a map containing the criteria for retrieving the record
   * @return the retrieved record of type T
   */
  protected abstract List<T> retrieveRecordsWithCriteria(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException;

  /**
   * Deletes a record from the specified table by its ID.
   *
   * @param tableName the name of the table
   * @param id the ID of the record to be deleted
   * @return the number of rows affected by the deletion
   * @throws SQLException if an error occurs while deleting the record
   */
  public static int deleteRecordByTableAndId(
    DatabaseUtil databaseUtil,
    String tableName,
    int id
  ) throws SQLException {
    return databaseUtil.deleteRecordById(tableName, "id", id);
  }

  /**
   * Executes the given SQL query and returns a list of maps representing the result set.
   *
   * @param query the SQL query to be executed
   * @return a list of maps representing the result set
   * @throws SQLException if an error occurs while executing the query
   */
  public List<Map<String, Object>> executeQuery(String query)
    throws SQLException {
    return this.databaseUtil.executeQuery(query);
  }
}
