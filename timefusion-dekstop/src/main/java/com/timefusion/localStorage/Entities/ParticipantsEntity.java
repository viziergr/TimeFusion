package com.timefusion.localStorage.Entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.timefusion.localStorage.JsonUtils;
import com.timefusion.model.User;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParticipantsEntity implements JsonEntity {

  private int id;
  private String firstName;
  private String lastName;
  private String email;

  public static final String PARTICIPANTS_ENTITY_NAME = "participants";

  private static final Gson gson = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  public ParticipantsEntity() {}

  public ParticipantsEntity(
    int id,
    String firstName,
    String lastName,
    String email
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public ParticipantsEntity(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isParticipantEmpty() {
    if (firstName != null && !firstName.isEmpty()) {
      return false;
    }

    if (lastName != null && !lastName.isEmpty()) {
      return false;
    }

    if (email != null && !email.isEmpty()) {
      return false;
    }

    return true;
  }

  public static boolean isParticipantInEventEmpty(int eventId) {
    ParticipantsEntity[] participants = getParticipantsArray(eventId);

    if (participants != null && participants.length > 0) {
      return false;
    }

    return true;
  }

  public static ParticipantsEntity[] getParticipantsArray(int eventId) {
    try (FileReader fileReader = new FileReader(JsonUtils.JSON_FILENAME)) {
      JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();
      ParticipantsEntity[] participants = null;

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("events")) {
          JsonArray eventsArray = jsonObject.getAsJsonArray("events");

          for (JsonElement eventElement : eventsArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();

            if (
              eventObject.has("id") &&
              eventObject.get("id").getAsInt() == eventId
            ) {
              JsonArray participantsArray = eventObject.getAsJsonArray(
                "participants"
              );

              if (participantsArray != null) {
                participants = jsonArrayToParticipantsArray(participantsArray);
                break;
              }
            }
          }
        }
      }
      return participants;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void addParticipantEntityArray(int eventId) {
    try (FileReader fileReader = new FileReader(JsonUtils.JSON_FILENAME)) {
      JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("events")) {
          JsonArray eventsArray = jsonObject.getAsJsonArray("events");

          for (JsonElement eventElement : eventsArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();

            if (
              eventObject.has("id") &&
              eventObject.get("id").getAsInt() == eventId
            ) {
              JsonArray participantsArray = eventObject.getAsJsonArray(
                "participants"
              );

              if (participantsArray != null) {
                participantsArray.add(this.toJsonObject());
              }
              break;
            }
          }
        }
      }

      try (FileWriter fileWriter = new FileWriter(JsonUtils.JSON_FILENAME)) {
        gson.toJson(jsonArray, fileWriter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void deleteParticipantEntity(int eventId, int participantId) {
    try (FileReader fileReader = new FileReader(JsonUtils.JSON_FILENAME)) {
      JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("events")) {
          JsonArray eventsArray = jsonObject.getAsJsonArray("events");

          for (JsonElement eventElement : eventsArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();

            if (
              eventObject.has("id") &&
              eventObject.get("id").getAsInt() == eventId
            ) {
              JsonArray participantsArray = eventObject.getAsJsonArray(
                "participants"
              );

              if (participantsArray != null) {
                for (JsonElement participantElement : participantsArray) {
                  JsonObject participantObject = participantElement.getAsJsonObject();

                  if (
                    participantObject.has("id") &&
                    participantObject.get("id").getAsInt() == participantId
                  ) {
                    participantsArray.remove(participantElement);
                    break;
                  }
                }
              }
              break;
            }
          }
        }
      }

      try (FileWriter fileWriter = new FileWriter(JsonUtils.JSON_FILENAME)) {
        gson.toJson(jsonArray, fileWriter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void deleteAllParticipantEntities(int eventId) {
    try (FileReader fileReader = new FileReader(JsonUtils.JSON_FILENAME)) {
      JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

      for (JsonElement jsonElement : jsonArray) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("events")) {
          JsonArray eventsArray = jsonObject.getAsJsonArray("events");

          for (JsonElement eventElement : eventsArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();

            if (
              eventObject.has("id") &&
              eventObject.get("id").getAsInt() == eventId
            ) {
              if (eventObject.has("participants")) {
                eventObject.add("participants", new JsonArray());
              } else {}
              break;
            }
          }
        }
      }

      try (FileWriter fileWriter = new FileWriter(JsonUtils.JSON_FILENAME)) {
        gson.toJson(jsonArray, fileWriter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static JsonArray participantsArrayToJsonArray(
    ParticipantsEntity[] participants
  ) {
    JsonArray jsonArray = new JsonArray();
    if (participants != null && participants.length > 0) {
      for (ParticipantsEntity participant : participants) {
        if (participant.isParticipantEmpty()) {
          continue;
        }
        jsonArray.add(participant.toJsonObject());
      }
    }

    return jsonArray;
  }

  public static ParticipantsEntity[] jsonArrayToParticipantsArray(
    JsonArray jsonArray
  ) {
    ParticipantsEntity[] participants = new ParticipantsEntity[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject participantJson = jsonArray.get(i).getAsJsonObject();
      ParticipantsEntity participant = new ParticipantsEntity();

      if (participantJson.has("id")) {
        participant.setId(participantJson.get("id").getAsInt());
      }

      if (participantJson.has("first_name")) {
        participant.setFirstName(
          participantJson.get("first_name").getAsString()
        );
      }

      if (participantJson.has("last_name")) {
        participant.setLastName(participantJson.get("last_name").getAsString());
      }

      if (participantJson.has("email")) {
        participant.setEmail(participantJson.get("email").getAsString());
      }

      participants[i] = participant;
    }
    return participants;
  }

  @Override
  public JsonObject toJsonObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", id);
    jsonObject.addProperty("first_name", firstName);
    jsonObject.addProperty("last_name", lastName);
    jsonObject.addProperty("email", email);
    return jsonObject;
  }

  @Override
  public String toJson() {
    return new Gson().toJson(this);
  }

  @Override
  public String toString() {
    return (
      "ParticipantsEntity{" +
      "id=" +
      id +
      ", firstName=\"" +
      firstName +
      "\", lastName=\"" +
      lastName +
      "\", email=\"" +
      email +
      "\"}"
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ParticipantsEntity) {
      ParticipantsEntity other = (ParticipantsEntity) obj;
      return (
        other.getId() == id &&
        other.getFirstName().equals(firstName) &&
        other.getLastName().equals(lastName) &&
        other.getEmail().equals(email)
      );
    } else if (obj instanceof ParticipantsEntity[]) {
      for (ParticipantsEntity participant : (ParticipantsEntity[]) obj) {
        if (participant.equals(this)) {
          return true;
        }
      }
    }
    return false;
  }
}
