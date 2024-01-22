package com.timefusion.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The DatabaseUtil class provides utility methods for interacting with a MySQL database.
 * It includes methods for executing queries, inserting, updating, and deleting records,
 * as well as retrieving records based on specified criteria.
 *
 * <p> <B> All the interactions with the database need to be done through the DatabaseUtil class. </B> </p>
 * <p> <B> To do so all the DAOs should extend the GenericDao class which works with the DatabaseUtil class.</B> </p>
 */
public class DatabaseUtil implements AutoCloseable {

  private static final String DEFAULT_DATABASE_URL =
    "jdbc:mysql://192.168.56.81:3306/TimeFusion";
  private static final String DEFAULT_DATABASE_USER = "root";
  private static final String DEFAULT_DATABASE_PASSWORD = "root";

  private final String databaseUrl;
  private final String databaseUser;
  private final String databasePassword;
  private Connection connection;

  /**
   * Constructs a new DatabaseUtil object with default database credentials.
   *
   * @throws SQLException If a database access error occurs.
   */
  public DatabaseUtil() throws SQLException {
    this(
      DEFAULT_DATABASE_URL,
      DEFAULT_DATABASE_USER,
      DEFAULT_DATABASE_PASSWORD
    );
  }

  /**
   * Constructs a new DatabaseUtil object with the specified database credentials.
   *
   * @param databaseUrl     The URL of the database.
   * @param databaseUser    The username for the database.
   * @param databasePassword The password for the database.
   * @throws SQLException If a database access error occurs.
   */
  public DatabaseUtil(
    String databaseUrl,
    String databaseUser,
    String databasePassword
  ) throws SQLException {
    Objects.requireNonNull(databaseUrl, "Database URL cannot be null.");
    Objects.requireNonNull(databaseUser, "Database user cannot be null.");
    Objects.requireNonNull(
      databasePassword,
      "Database password cannot be null."
    );

    this.databaseUrl = databaseUrl;
    this.databaseUser = databaseUser;
    this.databasePassword = databasePassword;
    this.connection = getConnectionToDB();
  }

  /**
   * Establishes a database connection.
   *
   * @return The database connection.
   * @throws SQLException If a database access error occurs.
   */
  private Connection getConnectionToDB() throws SQLException {
    try {
      // Load the MySQL driver
      Class.forName("com.mysql.cj.jdbc.Driver");

      // Establish the connection
      return DriverManager.getConnection(
        this.databaseUrl,
        this.databaseUser,
        this.databasePassword
      );
    } catch (ClassNotFoundException e) {
      throw new SQLException(
        "Failed to load MySQL driver. Make sure the MySQL JDBC driver is on the classpath.",
        e
      );
    }
  }

  /**
   * Returns the database connection.
   *
   * @return The database connection.
   */
  private Connection getConnection() {
    return this.connection;
  }

  /**
   * Closes the database connection.
   */
  public void closeConnection() {
    try {
      if (getConnection() != null && !getConnection().isClosed()) {
        getConnection().close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes the database connection.
   * If the connection is not null and is not already closed, it will be closed.
   *
   * @throws SQLException if an error occurs while closing the connection.
   */
  @Override
  public void close() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  /**
   * Executes the given SQL query with the provided parameters and returns the result as a list of maps.
   *
   * @param sql    The SQL query to execute.
   * @param params The list of parameters to be set in the prepared statement.
   * @return A list of maps, where each map represents a row in the result set. The keys are column names, and values are corresponding column values.
   * @throws SQLException If an error occurs while executing the query.
   *
   * @implNote This function can work without the params parameter and can be used for queries without parameters by calling executeQuery(sql) instead.
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *     try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *         String firstName = "Corentin";
   *         int id = 2;
   *         String query = "SELECT * FROM user WHERE first_name = ? AND id = ?";
   *
   *         List<Object> params = new ArrayList<>();
   *         params.add(firstName);
   *         params.add(id);
   *
   *         List<Map<String, Object>> resultList = databaseUtil.executeQuery(query, params);
   *
   *         for (Map<String, Object> row : resultList) {
   *             System.out.println("Row:");
   *             for (Map.Entry<String, Object> entry : row.entrySet()) {
   *                 String columnName = entry.getKey();
   *                 Object value = entry.getValue();
   *                 System.out.println(columnName + ": " + value);
   *             }
   *             System.out.println();
   *         }
   *     } catch (SQLException e) {
   *         e.printStackTrace();
   *     }
   * }
   * }</pre>
   */
  public List<Map<String, Object>> executeQuery(
    String sql,
    List<Object> params
  ) throws SQLException {
    List<Map<String, Object>> resultList = new ArrayList<>();

    try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
      if (params != null) {
        setParameters(statement, params);
      }

      try (ResultSet resultSet = statement.executeQuery()) {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
          Map<String, Object> row = new HashMap<>();
          for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i).toLowerCase(); // Convert to lowercase
            Object value = resultSet.getObject(i);
            row.put(columnName, value);
          }
          resultList.add(row);
        }
      } finally {
        statement.close();
      }
    }

    return resultList;
  }

  /**
   * Executes the given SQL query and returns the result as a list of maps.
   *
   * @param sql The SQL query to execute.
   * @return A list of maps, where each map represents a row in the result set. The keys are column names, and values are corresponding column values.
   * @throws SQLException If an error occurs while executing the query.
   *
   * @implNote This function can be used for queries without parameters.
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *     try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *         String query = "SELECT * FROM user";
   *
   *         List<Map<String, Object>> resultList = databaseUtil.executeQuery(query);
   *
   *         for (Map<String, Object> row : resultList) {
   *             System.out.println("Row:");
   *             for (Map.Entry<String, Object> entry : row.entrySet()) {
   *                 String columnName = entry.getKey();
   *                 Object value = entry.getValue();
   *                 System.out.println(columnName + ": " + value);
   *             }
   *             System.out.println();
   *         }
   *     } catch (SQLException e) {
   *         e.printStackTrace();
   *     }
   * }
   * }</pre>
   */
  public List<Map<String, Object>> executeQuery(String sql)
    throws SQLException {
    return executeQuery(sql, null);
  }

  /**
   * Sets the parameters for a PreparedStatement.
   *
   * @param statement the PreparedStatement to set the parameters for
   * @param params the list of parameters to set
   * @throws SQLException if an error occurs while setting the parameters
   */
  private void setParameters(PreparedStatement statement, List<Object> params)
    throws SQLException {
    Objects.requireNonNull(statement, "PreparedStatement cannot be null.");

    for (int i = 0; i < params.size(); i++) {
      statement.setObject(i + 1, params.get(i));
    }
  }

  /**
   * Inserts a record into the specified table with the given column values.
   *
   * @param tableName     the name of the table to insert the record into
   * @param columnValues  a map containing the column names and their corresponding values
   * @return the ID of the inserted record
   * @throws SQLException if a database access error occurs
   * @throws IllegalArgumentException if the columnValues map is empty or if the tableName is empty
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *    try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *       String tableName = "user";
   *       Map<String, Object> columnValues = new HashMap<>();
   *       columnValues.put("first_name", "John");
   *       columnValues.put("last_name", "Doe");
   *       columnValues.put("email", "corentin.robin44@gmail.com");
   *       columnValues.put("password", EncryptionUtil.hashPassword("password"));
   *
   *       try {
   *          int rowsAffected = databaseUtil.insertRecord(tableName, columnValues);
   *          System.out.println(rowsAffected + " row(s) affected by the insert operation.");
   *       } catch (SQLException | IllegalArgumentException e) {
   *          e.printStackTrace();
   *      }
   *    } catch (SQLException e) {
   *      e.printStackTrace();
   *   }
   * }</pre>
   */
  public int insertRecord(String tableName, Map<String, Object> columnValues)
    throws SQLException, IllegalArgumentException {
    if (columnValues.isEmpty()) {
      throw new IllegalArgumentException("Column values map is empty.");
    }
    if (tableName.isEmpty()) {
      throw new IllegalArgumentException("Table name is empty.");
    }

    StringBuilder insertSql = new StringBuilder(
      "INSERT INTO " + tableName + " ("
    );
    StringBuilder valuesPlaceholder = new StringBuilder(") VALUES (");

    for (String columnName : columnValues.keySet()) {
      insertSql.append(columnName).append(",");
      valuesPlaceholder.append("?,");
    }

    insertSql.deleteCharAt(insertSql.length() - 1);
    valuesPlaceholder.deleteCharAt(valuesPlaceholder.length() - 1);

    insertSql.append(valuesPlaceholder).append(")");

    try (
      PreparedStatement statement = getConnection()
        .prepareStatement(
          insertSql.toString(),
          PreparedStatement.RETURN_GENERATED_KEYS
        )
    ) {
      int parameterIndex = 1;
      for (Object value : columnValues.values()) {
        statement.setObject(parameterIndex++, value);
      }

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
          } else {
            throw new SQLException("Failed to retrieve the generated ID.");
          }
        }
      } else {
        throw new SQLException("Insert operation did not affect any rows.");
      }
    }
  }

  /**
   * Updates a record in the specified table based on the record ID.
   *
   * @implNote <B>  This function does not required all the columns to be updated. </B>
   *
   * @param tableName       The name of the table to update the record in.
   * @param idColumnName    The name of the column representing the record ID.
   * @param recordId        The unique identifier of the record to be updated.
   * @param columnValues    A map where keys are column names, and values are the new values
   *                        to be set for the respective columns.
   * @return The number of rows affected by the update (0 or 1).
   * @throws SQLException   If a database access error occurs.
   * @throws IllegalArgumentException If the columnValues map is empty, if the tableName is empty or if the idColumnName is empty.
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *   try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *    String tableName = "user";
   *    String idColumnName = "id";
   *    long recordIdToUpdate = 3;
   *
   *    Map<String, Object> updatedColumnValues = new HashMap<>();
   *
   *    updatedColumnValues.put("first_name", "UpdatedFirstName");
   *    updatedColumnValues.put("last_name", "UpdatedLastName");
   *
   *    try {
   *      int rowsAffected = databaseUtil.updateRecordById(
   *        tableName,
   *        idColumnName,
   *        recordIdToUpdate,
   *        updatedColumnValues
   *      );
   *
   *       System.out.println(
   *         rowsAffected + " row(s) affected by the update operation."
   *       );
   *
   *      } catch (SQLException | IllegalArgumentException e) {
   *         e.printStackTrace();
   *        }
   *     } catch (SQLException e) {
   *      e.printStackTrace();
   *    }
   * }</pre>
   *
   */
  public int updateRecordById(
    String tableName,
    String idColumnName,
    int recordId,
    Map<String, Object> columnValues
  ) throws SQLException, IllegalArgumentException {
    if (tableName.isEmpty()) {
      throw new IllegalArgumentException("Table name is empty.");
    }
    if (idColumnName.isEmpty()) {
      throw new IllegalArgumentException("ID column name is empty.");
    }
    if (columnValues.isEmpty()) {
      throw new IllegalArgumentException("Column values map is empty.");
    }

    StringBuilder updateSql = new StringBuilder(
      "UPDATE " + tableName + " SET "
    );

    for (String columnName : columnValues.keySet()) {
      updateSql.append(columnName).append("=?,");
    }

    updateSql.deleteCharAt(updateSql.length() - 1);

    updateSql.append(" WHERE ").append(idColumnName).append(" = ?");

    try (
      PreparedStatement statement = getConnection()
        .prepareStatement(updateSql.toString())
    ) {
      int parameterIndex = 1;
      for (Object value : columnValues.values()) {
        statement.setObject(parameterIndex++, value);
      }

      statement.setInt(parameterIndex, recordId);

      return statement.executeUpdate();
    }
  }

  /**
   * Deletes a record from the specified table based on the record ID.
   *
   * @param tableName       The name of the table from which to delete the record.
   * @param idColumnName    The name of the column representing the record ID.
   * @param recordId        The unique identifier of the record to be deleted.
   * @return The number of rows affected by the deletion (0 or 1).
   * @throws SQLException   If a database access error occurs.
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *   try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *     String tableName = "user";
   *     String idColumnName = "id";
   *     long recordIdToDelete = 3; // Replace with the actual ID of the record you want to delete
   *
   *     try {
   *       int rowsAffected = databaseUtil.deleteRecordById(
   *         tableName,
   *         idColumnName,
   *         recordIdToDelete
   *       );
   *
   *       System.out.println(
   *         rowsAffected + " row(s) affected by the delete operation."
   *       );
   *     } catch (SQLException | IllegalArgumentException e) {
   *       e.printStackTrace();
   *     }
   *    } catch (SQLException e) {
   *     e.printStackTrace();
   *  }
   * }</pre>
   */
  public int deleteRecordById(
    String tableName,
    String idColumnName,
    int recordId
  ) throws SQLException {
    if (tableName.isEmpty()) {
      throw new IllegalArgumentException("Table name is empty.");
    }
    if (idColumnName.isEmpty()) {
      throw new IllegalArgumentException("ID column name is empty.");
    }

    String deleteSql =
      "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";

    try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
      statement.setInt(1, recordId);

      return statement.executeUpdate();
    }
  }

  /**
   * Retrieves records from the specified table based on the provided criteria.
   *
   * @param tableName    The name of the table to retrieve records from.
   * @param criteriaMap  A map containing criteria for the WHERE clause. Each key represents a column name,
   *                     and the corresponding value is the expected value for that column.
   * @return A list of maps, where each map represents a row in the result set. The keys are column names,
   *         and values are corresponding column values.
   * @throws SQLException              If a database access error occurs.
   * @throws IllegalArgumentException  If the criteriaMap is null or empty.
   *
   * @example
   * <p>Usage in the main method:</p>
   * <pre>{@code
   * public static void main(String[] args) {
   *     try (DatabaseUtil databaseUtil = new DatabaseUtil()) {
   *         String tableName = "user"; // Replace with the actual table name
   *         Map<String, Object> criteriaMap = new HashMap<>();
   *         criteriaMap.put("first_name", "corentin"); // Replace with your actual criteria
   *
   *         try {
   *             List<Map<String, Object>> resultList = databaseUtil.retrieveRecords(tableName, criteriaMap);
   *
   *             for (Map<String, Object> row : resultList) {
   *                 System.out.println("Row:");
   *                 for (Map.Entry<String, Object> entry : row.entrySet()) {
   *                     String columnName = entry.getKey();
   *                     Object value = entry.getValue();
   *                     System.out.println(columnName + ": " + value);
   *                 }
   *                 System.out.println();
   *             }
   *         } catch (SQLException | IllegalArgumentException e) {
   *             e.printStackTrace();
   *         }
   *     } catch (SQLException e) {
   *         e.printStackTrace();
   *     }
   * }
   * }</pre>
   */
  public List<Map<String, Object>> retrieveRecords(
    String tableName,
    Map<String, Object> criteriaMap
  ) throws SQLException {
    if (criteriaMap == null || criteriaMap.isEmpty()) {
      throw new IllegalArgumentException(
        "Criteria map must not be null or empty."
      );
    }

    StringBuilder sql = new StringBuilder("SELECT * FROM ")
      .append(tableName)
      .append(" WHERE ");

    for (Map.Entry<String, Object> entry : criteriaMap.entrySet()) {
      sql.append(entry.getKey()).append(" = ? AND ");
    }

    // Remove the trailing "AND"
    sql.setLength(sql.length() - 5);

    return this.executeQuery(
        sql.toString(),
        new ArrayList<>(criteriaMap.values())
      );
  }

  public static String queryToString(List<Map<String, Object>> resultList) {
    StringBuilder sb = new StringBuilder();
    for (Map<String, Object> row : resultList) {
      for (Map.Entry<String, Object> entry : row.entrySet()) {
        String columnName = entry.getKey();
        Object value = entry.getValue();
        sb.append(columnName).append(": ").append(value).append("\n");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
