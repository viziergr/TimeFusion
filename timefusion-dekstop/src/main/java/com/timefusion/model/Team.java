package com.timefusion.model;

public class Team {

  private int id;
  private String name;
  private String description;
  private String color;

  /**
   * Represents a team in the application.
   * @param id the id of the team
   * @param name the name of the team
   * @param description the description of the team
   * @param color the color of the team
   */
  public Team(int id, String name, String description, String color) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.color = color;
  }

  /**
   * Represents a team in the application.
   *
   * @param name        the name of the team
   * @param description the description of the team
   * @param color       the color associated with the team
   */
  public Team(String name, String description, String color) {
    this(0, name, description, color);
  }

  /**
   * Constructor for Team
   * @param name
   * @param description
   */
  public Team(String name, String description) {
    this(0, name, description, null);
  }

  /**
   * Returns the ID of the team.
   * @return the ID of the team
   */
  public int getId() {
    return this.id;
  }

  /**
   * Returns the name of the team.
   * @return the name of the team
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the description of the team.
   * @return the description of the team
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the ID of the team.
   *
   * @param id the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the name of the team.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the description of the team.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the color of the team.
   *
   * @return the color of the team as a String.
   */
  public String getColor() {
    return this.color;
  }

  /**
   * Sets the color of the team.
   *
   * @param color the color to set
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * Returns a string representation of the Team object.
   *
   * @return The string representation of the Team object.
   */
  @Override
  public String toString() {
    return String.format(
      "Team{id=%d, name='%s', description='%s', color='%s'}",
      id,
      name,
      description,
      color
    );
  }

  /**
   * Compares this Team object with the specified object for equality.
   * Returns true if the given object is also a Team and has the same values for id, name, description, and color.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Team)) return false;
    Team team = (Team) o;
    return (
      this.id == team.id &&
      this.name.equals(team.name) &&
      this.description.equals(team.description) &&
      this.color.equals(team.color)
    );
  }
}
