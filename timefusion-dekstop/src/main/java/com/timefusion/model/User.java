package com.timefusion.model;

import java.util.Objects;

public class User {

  private int id;
  private String email;
  private String firstName;
  private String lastName;
  private String password;

  /**
   * Constructor for User
   *
   * @param id
   * @param email
   * @param firstName
   * @param lastName
   * @param password
   * @param year
   */
  public User(
    int id,
    String email,
    String firstName,
    String lastName,
    String password
  ) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
  }

  /**
   * Constructor for User
   *
   * @param email
   * @param firstName
   * @param lastName
   * @param password
   */
  public User(
    String email,
    String firstName,
    String lastName,
    String password
  ) {
    this(0, email, firstName, lastName, password);
  }

  /**
   * Returns the ID of the user.
   *
   * @return the ID of the user
   */
  public int getId() {
    return this.id;
  }

  /**
   * Returns the email of the user.
   *
   * @return the email of the user
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Returns the first name of the user.
   *
   * @return the first name of the user
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Returns the last name of the user.
   *
   * @return the last name of the user
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password of the user
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Sets the ID of the user.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /*
   * Sets the email of the user.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /*
   * Sets the first name of the user.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /*
   * Sets the last name of the user.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /*
   * Sets the password of the user.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns a string representation of the User object.
   *
   * @return a string representation of the User object
   */
  @Override
  public String toString() {
    return (
      "User{" +
      "id=" +
      this.id +
      ", email='" +
      this.email +
      '\'' +
      ", firstName='" +
      this.firstName +
      '\'' +
      ", lastName='" +
      this.lastName +
      '\'' +
      ", password='" +
      this.password +
      '\'' +
      '}'
    );
  }

  /**
   * Compares this User object with the specified object for equality.
   * Returns true if the given object is also a User object and has the same id, email, firstName,
   * lastName, and password as this User object.
   *
   * @param obj the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    User user = (User) obj;
    return (
      id == user.id &&
      Objects.equals(email, user.email) &&
      Objects.equals(firstName, user.firstName) &&
      Objects.equals(lastName, user.lastName) &&
      Objects.equals(password, user.password)
    );
  }
}
