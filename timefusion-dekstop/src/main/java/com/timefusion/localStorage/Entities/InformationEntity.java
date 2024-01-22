package com.timefusion.localStorage.Entities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.timefusion.localStorage.JsonUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InformationEntity implements JsonEntity {

  public static final String INFORMATION_ENTITY_NAME = "information";
  public static final int INFORMATION_ENTITY_POSITION = 0;
  private String lastUpdated;
  private String lastSynced;

  public InformationEntity() {}

  public InformationEntity(String lastUpdated, String lastSynced) {
    this.lastUpdated = lastUpdated;
    this.lastSynced = lastSynced;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdatedNow() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "dd-MM-yyyy HH:mm:ss"
    );
    this.lastUpdated = LocalDateTime.now().format(formatter).toString();
  }

  public String getLastSynced() {
    return lastSynced;
  }

  public void setLastSyncedNow() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "dd-MM-yyyy HH:mm:ss"
    );
    this.lastSynced = LocalDateTime.now().format(formatter).toString();
  }

  public static boolean isJsonInformationEntityEmpty() {
    JsonElement informationJsonElement = JsonUtils.readJsonPart(
      INFORMATION_ENTITY_NAME
    );

    if (informationJsonElement.isJsonObject()) {
      JsonObject informationObject = informationJsonElement.getAsJsonObject();
      return informationObject.entrySet().isEmpty();
    }

    return true;
  }

  public void addInformationEntity() {
    JsonElement existingInformation = JsonUtils.readJsonPart(
      INFORMATION_ENTITY_NAME
    );

    if (
      existingInformation != null &&
      existingInformation.getAsJsonObject().entrySet().isEmpty()
    ) {
      JsonUtils.addEntityElement(
        JsonUtils.JSON_FILENAME,
        INFORMATION_ENTITY_NAME,
        this.toJsonObject(),
        INFORMATION_ENTITY_POSITION
      );
    }
  }

  public void updateInformationEntity() {
    JsonUtils.updateEntityElement(
      JsonUtils.JSON_FILENAME,
      INFORMATION_ENTITY_NAME,
      this.toJsonObject()
    );
  }

  public static void deleteInformationEntity() {
    JsonUtils.deleteEntityElement(
      JsonUtils.JSON_FILENAME,
      INFORMATION_ENTITY_NAME,
      INFORMATION_ENTITY_POSITION
    );
  }

  public static InformationEntity getInfoEntityFromJson() {
    JsonElement infoJsonElement = JsonUtils.readJsonPart(
      INFORMATION_ENTITY_NAME
    );

    if (infoJsonElement.isJsonObject()) {
      JsonObject jsonObject = infoJsonElement.getAsJsonObject();
      if (!jsonObject.entrySet().isEmpty()) {
        return new InformationEntity(
          jsonObject.get("last_updated").getAsString(),
          jsonObject.get("last_synced").getAsString()
        );
      }
    }

    return new InformationEntity();
  }

  public static InformationEntity getInformationEntity() {
    JsonObject jsonObject = JsonUtils
      .readJsonPart(INFORMATION_ENTITY_NAME)
      .getAsJsonObject();
    return new InformationEntity(
      jsonObject.get("last_updated").getAsString(),
      jsonObject.get("last_synced").getAsString()
    );
  }

  @Override
  public JsonObject toJsonObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("last_updated", lastUpdated);
    jsonObject.addProperty("last_synced", lastSynced);
    return jsonObject;
  }

  @Override
  public String toString() {
    return (
      "InformationEntity{" +
      "last_updated=" +
      lastUpdated +
      ", last_synced=" +
      lastSynced +
      '}'
    );
  }

  @Override
  public String toJson() {
    return new Gson().toJson(this);
  }
}
