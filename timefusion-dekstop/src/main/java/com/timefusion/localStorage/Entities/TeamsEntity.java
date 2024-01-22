package com.timefusion.localStorage.Entities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.timefusion.localStorage.JsonUtils;

public class TeamsEntity implements JsonEntity {

  public static final String TEAMS_ENTITY_NAME = "teams";
  public static final int TEAMS_ENTITY_POSITION = 2;

  private int id;
  private String name;
  private String description;

  public TeamsEntity() {}

  public TeamsEntity(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static TeamsEntity getTeamEntityById(int id) {
    JsonElement teamJsonElement = JsonUtils.readJsonPart(TEAMS_ENTITY_NAME);

    if (teamJsonElement.isJsonArray()) {
      JsonArray teamJsonArray = teamJsonElement.getAsJsonArray();

      for (JsonElement teamElement : teamJsonArray) {
        JsonObject teamObject = teamElement.getAsJsonObject();
        int teamId = teamObject.get("id").getAsInt();

        if (teamId == id) {
          return new Gson().fromJson(teamObject, TeamsEntity.class);
        }
      }
    }

    return null;
  }

  public static JsonArray getAllTeamEntities() {
    JsonElement teamJsonElement = JsonUtils.readJsonPart(TEAMS_ENTITY_NAME);

    if (teamJsonElement.isJsonArray()) {
      return teamJsonElement.getAsJsonArray();
    }

    return null;
  }

  public void addTeamEntity() {
    JsonUtils.addEntityArray(
      JsonUtils.JSON_FILENAME,
      TEAMS_ENTITY_NAME,
      this.toJsonObject()
    );
  }

  public void updateTeamEntity() {
    JsonUtils.updateEntityArray(
      JsonUtils.JSON_FILENAME,
      TEAMS_ENTITY_NAME,
      this.getId(),
      this.toJsonObject()
    );
  }

  public static void deleteTeamEntity(int id) {
    JsonUtils.deleteEntityArray(JsonUtils.JSON_FILENAME, TEAMS_ENTITY_NAME, id);
  }

  public static void deleteAllTeamEntities() {
    JsonArray teamJsonArray = getAllTeamEntities();
    for (JsonElement teamElement : teamJsonArray) {
      JsonObject teamObject = teamElement.getAsJsonObject();
      int teamId = teamObject.get("id").getAsInt();
      TeamsEntity.deleteTeamEntity(teamId);
    }
  }

  public static boolean isJsonTeamEntityEmpty() {
    JsonElement teamJsonElement = JsonUtils.readJsonPart(TEAMS_ENTITY_NAME);

    if (teamJsonElement.isJsonArray()) {
      JsonArray teamJsonArray = teamJsonElement.getAsJsonArray();
      return teamJsonArray.size() == 0;
    } else {
      return true;
    }
  }

  public JsonObject toJsonObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", id);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("description", description);
    return jsonObject;
  }

  @Override
  public String toString() {
    return (
      "TeamsEntity{" +
      "id=" +
      id +
      ", name='" +
      name +
      '\'' +
      ", description='" +
      description +
      '\'' +
      '}'
    );
  }

  @Override
  public String toJson() {
    return new Gson().toJson(this);
  }
}
