package com.timefusion.localStorage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.timefusion.localStorage.Entities.EventsEntity;
import com.timefusion.localStorage.Entities.InformationEntity;
import com.timefusion.localStorage.Entities.TeamsEntity;
import com.timefusion.localStorage.Entities.UserEntity;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {

  public static final String JSON_FILENAME =
    "timefusion-dekstop/src/main/java/com/timefusion/localStorage/LocalStorage.json";

  private static final Gson gson = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  public static void printJson() {
    try {
      Path filePath = Path.of(JSON_FILENAME);
      Files.readAllBytes(filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isLocalStorageEmpty() {
    return (
      InformationEntity.isJsonInformationEntityEmpty() &&
      UserEntity.isJsonUserEntityEmpty() &&
      TeamsEntity.isJsonTeamEntityEmpty() &&
      EventsEntity.isJsonEventsEntityEmpty()
    );
  }

  /**
   * Reads a specific part from a JSON file and returns it as a JsonElement.
   *
   * @param partName the name of the part to read from the JSON file
   * @return the JsonElement representing the specified part, or null if the part is not found
   */
  public static JsonElement readJsonPart(String partName) {
    try {
      Path filePath = Path.of(JSON_FILENAME);
      byte[] jsonData = Files.readAllBytes(filePath);
      String jsonString = new String(jsonData);
      JsonElement jsonElement = JsonParser.parseString(jsonString);

      if (jsonElement.isJsonObject()) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has(partName)) {
          return jsonObject.get(partName);
        }
      } else if (jsonElement.isJsonArray()) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
          JsonObject jsonObject = element.getAsJsonObject();
          if (jsonObject.has(partName)) {
            return jsonObject.get(partName);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Converts an object to its JSON representation.
   *
   * @param object the object to be converted
   * @return the JSON representation of the object
   */
  public static String convertObjectToJson(Object object) {
    return gson.toJson(object);
  }

  /**
   * Adds a new entity to the JSON array stored in the specified file path.
   *
   * @param filePath   The path of the JSON file.
   * @param entityName The name of the entity array.
   * @param newEntity  The new entity to be added.
   */
  public static void addEntityArray(
    String filePath,
    String entityName,
    JsonObject newEntity
  ) {
    JsonArray jsonArray = readFromFile(filePath);

    JsonObject jsonRoot = getOrCreateEntityArray(jsonArray, entityName);

    jsonRoot.getAsJsonArray(entityName).add(newEntity);

    writeToFile(jsonArray, filePath);
  }

  /**
   * Adds an entity element to a JSON file at a specified position.
   *
   * @param filePath    The path of the JSON file.
   * @param entityName  The name of the entity to be added.
   * @param newEntity   The JsonObject representing the new entity.
   * @param position    The position at which the entity should be added in the JSON array.
   */
  public static void addEntityElement(
    String filePath,
    String entityName,
    JsonObject newEntity,
    int position
  ) {
    JsonArray jsonArray = readFromFile(filePath);
    JsonObject jsonRoot = null;

    if (jsonArray.isJsonArray()) {
      jsonRoot = jsonArray.get(position).getAsJsonObject();
    }

    if (jsonRoot.has(entityName)) {
      jsonRoot.add(entityName, newEntity);
    } else {
      JsonObject informationObject = jsonRoot.getAsJsonObject(entityName);
      if (informationObject == null) {
        informationObject = new JsonObject();
        jsonRoot.add(entityName, informationObject);
      }
      informationObject.add(entityName, newEntity);
    }

    writeToFileElement(jsonArray, filePath);
  }

  /**
   * Updates an entity in a JSON array based on its ID.
   *
   * @param filePath      The path to the JSON file.
   * @param entityName    The name of the entity.
   * @param entityId      The ID of the entity to be updated.
   * @param updatedEntity The updated entity object.
   */
  public static void updateEntityArray(
    String filePath,
    String entityName,
    int entityId,
    JsonObject updatedEntity
  ) {
    JsonArray jsonArray = readFromFile(filePath);

    JsonObject jsonRoot = getEntityArray(jsonArray, entityName);
    if (jsonRoot == null) {
      return;
    }

    JsonArray entityArray = jsonRoot.getAsJsonArray(entityName);
    for (JsonElement element : entityArray) {
      JsonObject entity = element.getAsJsonObject();
      if (entity.has("id") && entity.get("id").getAsInt() == entityId) {
        entityArray.remove(element);
        entityArray.add(updatedEntity);
        break;
      }
    }

    writeToFile(jsonArray, filePath);
  }

  /**
   * Updates the specified entity element in a JSON file.
   *
   * @param filePath The path to the JSON file.
   * @param entityName The name of the entity to be updated.
   * @param updatedEntity The updated entity object containing the new values.
   */
  public static void updateEntityElement(
    String filePath,
    String entityName,
    JsonObject updatedEntity
  ) {
    JsonArray jsonArray = readFromFile(filePath);
    JsonObject jsonRoot = null;
    JsonElement jsonElement = jsonArray.get(0);

    if (jsonElement.isJsonObject()) {
      jsonRoot = jsonElement.getAsJsonObject();
    } else {
      return;
    }

    if (jsonRoot.has(entityName)) {
      JsonObject existingEntity = jsonRoot.getAsJsonObject(entityName);

      for (String property : updatedEntity.keySet()) {
        existingEntity.add(property, updatedEntity.get(property));
      }
    }

    writeToFileElement(jsonArray, filePath);
  }

  /**
   * Deletes an entity from a JSON file based on its ID.
   *
   * @param filePath   The path of the JSON file.
   * @param entityName The name of the entity.
   * @param entityId   The ID of the entity to be deleted.
   */
  public static void deleteEntityArray(
    String filePath,
    String entityName,
    int entityId
  ) {
    JsonArray jsonArray = readFromFile(filePath);

    JsonObject jsonRoot = getEntityArray(jsonArray, entityName);
    if (jsonRoot == null) {
      return;
    }

    JsonArray entityArray = jsonRoot.getAsJsonArray(entityName);
    for (JsonElement element : entityArray) {
      JsonObject entity = element.getAsJsonObject();
      if (entity.has("id") && entity.get("id").getAsInt() == entityId) {
        entityArray.remove(element);
        break;
      }
    }

    writeToFile(jsonArray, filePath);
  }

  /**
   * Deletes the specified entity element from a JSON file.
   *
   * @param filePath    The path of the JSON file.
   * @param entityName  The name of the entity to be deleted.
   * @param position    The position of the entity element in the JSON array.
   */
  public static void deleteEntityElement(
    String filePath,
    String entityName,
    int position
  ) {
    JsonArray jsonArray = readFromFile(filePath);

    if (jsonArray.isJsonArray() && jsonArray.size() > 0) {
      JsonElement firstElement = jsonArray.get(position);

      if (firstElement.isJsonObject()) {
        JsonObject jsonRoot = firstElement.getAsJsonObject();

        if (jsonRoot.has(entityName)) {
          JsonElement entityElement = jsonRoot.get(entityName);
          if (entityElement.isJsonObject()) {
            JsonObject entityObject = entityElement.getAsJsonObject();
            entityObject.entrySet().removeIf(entry -> true); // Remove all properties
          }
        }

        writeToFileElement(jsonArray, filePath);
      }
    }
  }

  /**
   * Deletes all entities in the JSON file.
   * This method deletes all information entities, user entities, team entities, and event entities.
   * @param filePath The path of the JSON file.
   */
  public static void deleteAllEntities() {
    InformationEntity.deleteInformationEntity();
    UserEntity.deleteUserEntity();
    TeamsEntity.deleteAllTeamEntities();
    EventsEntity.deleteAllEventEntities();
  }

  /**
   * Reads a JSON array from a file.
   *
   * @param filePath the path of the file to read from
   * @return the JSON array read from the file, or an empty JSON array if an error occurs
   */
  private static JsonArray readFromFile(String filePath) {
    try (FileReader fileReader = new FileReader(filePath)) {
      return JsonParser.parseReader(fileReader).getAsJsonArray();
    } catch (IOException e) {
      e.printStackTrace();
      return new JsonArray();
    }
  }

  /**
   * Writes a JsonArray to a file.
   *
   * @param jsonArray the JsonArray to write
   * @param filePath the path of the file to write to
   */
  private static void writeToFile(JsonArray jsonArray, String filePath) {
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      gson.toJson(jsonArray, fileWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the given JsonElement to a file at the specified file path.
   *
   * @param jsonElement The JsonElement to be written to the file.
   * @param filePath The path of the file to write the JsonElement to.
   */
  private static void writeToFileElement(
    JsonElement jsonElement,
    String filePath
  ) {
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      gson.toJson(jsonElement, fileWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Represents a JSON object, which is a collection of key-value pairs.
   * This class provides methods to manipulate and access the elements of a JSON object.
   */
  private static JsonObject getOrCreateEntityArray(
    JsonArray jsonArray,
    String entityName
  ) {
    for (JsonElement jsonElement : jsonArray) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      if (jsonObject.has(entityName)) {
        return jsonObject;
      }
    }

    JsonObject newEntityArray = new JsonObject();
    newEntityArray.add(entityName, new JsonArray());
    jsonArray.add(newEntityArray);
    return newEntityArray;
  }

  /**
   * Retrieves the first JsonObject in a JsonArray that contains a specific entity name.
   *
   * @param jsonArray  The JsonArray to search in.
   * @param entityName The name of the entity to look for.
   * @return The JsonObject that contains the specified entity name, or null if not found.
   */
  private static JsonObject getEntityArray(
    JsonArray jsonArray,
    String entityName
  ) {
    for (JsonElement jsonElement : jsonArray) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      if (jsonObject.has(entityName)) {
        return jsonObject;
      }
    }
    return null;
  }
}
