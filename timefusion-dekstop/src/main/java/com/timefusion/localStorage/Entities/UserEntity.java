package com.timefusion.localStorage.Entities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.timefusion.localStorage.JsonUtils;
import com.timefusion.model.User;

public class UserEntity implements JsonEntity {

  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public static final String USER_ENTITY_NAME = "user";
  public static final int USER_ENTITY_POSITION = 1;

  public UserEntity() {}

  public UserEntity(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.password = user.getPassword();
  }

  public UserEntity(
    int id,
    String firstName,
    String lastName,
    String email,
    String password
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword() {
    this.password = "password";
  }

  public static boolean isJsonUserEntityEmpty() {
    JsonElement userJsonElement = JsonUtils.readJsonPart(USER_ENTITY_NAME);

    if (userJsonElement.isJsonObject()) {
      JsonObject userObject = userJsonElement.getAsJsonObject();
      return userObject.entrySet().isEmpty();
    }

    return true;
  }

  public void addUserEntity() {
    JsonElement existingInformation = JsonUtils.readJsonPart(USER_ENTITY_NAME);

    if (
      existingInformation != null &&
      existingInformation.getAsJsonObject().entrySet().isEmpty()
    ) {
      JsonUtils.addEntityElement(
        JsonUtils.JSON_FILENAME,
        USER_ENTITY_NAME,
        this.toJsonObject(),
        USER_ENTITY_POSITION
      );
    }
  }

  public void updateUserEntity() {
    JsonUtils.addEntityElement(
      JsonUtils.JSON_FILENAME,
      USER_ENTITY_NAME,
      this.toJsonObject(),
      USER_ENTITY_POSITION
    );
  }

  public static void deleteUserEntity() {
    JsonUtils.deleteEntityElement(
      JsonUtils.JSON_FILENAME,
      USER_ENTITY_NAME,
      USER_ENTITY_POSITION
    );
  }

  public static UserEntity getuserEntityFromJson() {
    JsonElement userJsonElement = JsonUtils.readJsonPart(USER_ENTITY_NAME);
    if (!UserEntity.isJsonUserEntityEmpty() && userJsonElement.isJsonObject()) {
      JsonObject userObject = userJsonElement.getAsJsonObject();
      UserEntity userEntity = new Gson().fromJson(userObject, UserEntity.class);
      userEntity.setFirstName(userObject.get("first_name").getAsString());
      userEntity.setLastName(userObject.get("last_name").getAsString());
      return userEntity;
    } else {
      return new UserEntity();
    }
  }

  public User toUser() {
    return new User(id, firstName, lastName, email, password);
  }

  public JsonObject toJsonObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", id);
    jsonObject.addProperty("first_name", firstName);
    jsonObject.addProperty("last_name", lastName);
    jsonObject.addProperty("email", email);
    jsonObject.addProperty("password", password);
    return jsonObject;
  }

  public static UserEntity userToUserEntity(User user) {
    return new UserEntity(
      user.getId(),
      user.getFirstName(),
      user.getLastName(),
      user.getEmail(),
      user.getPassword()
    );
  }

  @Override
  public String toJson() {
    return new Gson().toJson(this);
  }

  @Override
  public String toString() {
    return (
      "UserEntity{" +
      "id=" +
      id +
      ", firstName='" +
      firstName +
      '\'' +
      ", lastName='" +
      lastName +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", password='" +
      password +
      '\'' +
      '}'
    );
  }
}
