package com.timefusion.model;

import java.util.Objects;

/**
 * Represents a team membership in the application.
 */
public class TeamMembership {

  private int id;
  private int userId;
  private int teamId;
  private String role;

  /**
   * Constructor for TeamMembership
   * @param id
   * @param userId
   * @param teamId
   * @param role
   */
  public TeamMembership(int id, int userId, int teamId, String role) {
    this.id = id;
    this.userId = userId;
    this.teamId = teamId;
    this.role = role;
  }

  /**
   * Represents a membership of a user in a team.
   *
   * @param userId the ID of the user
   * @param teamId the ID of the team
   */
  public TeamMembership(int userId, int teamId) {
    this(0, userId, teamId, "Member");
  }

  /**
   * Represents a team membership.
   *
   * This class stores information about a user's membership in a team, including the user ID, team ID, and color.
   */
  public TeamMembership(int userId, int teamId, String color) {
    this(0, userId, teamId, color);
  }

  /**
   * Returns the ID of the team membership.
   * @return the ID of the team membership
   */
  public int getId() {
    return this.id;
  }

  /**
   * Returns the ID of the user.
   * @return the ID of the user
   */
  public int getUserId() {
    return this.userId;
  }

  /**
   * Returns the ID of the team.
   * @return the ID of the team
   */
  public int getTeamId() {
    return this.teamId;
  }

  /**
   * Returns the role of the user in the team.
   * @return the role of the user in the team
   */
  public String getRole() {
    return this.role;
  }

  /**
   * Sets the role of the user in the team.
   * @param role the role of the user in the team
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Sets the ID of the team membership.
   * @param id the ID of the team membership
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the ID of the user.
   * @param userId the ID of the user
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Sets the ID of the team.
   * @param teamId the ID of the team
   */
  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  /**
   * Returns a string representation of the team membership.
   * @return a string representation of the team membership
   */
  @Override
  public String toString() {
    return (
      "TeamMembership {id=" +
      id +
      ", userId=" +
      userId +
      ", teamId=" +
      teamId +
      ", role=" +
      role +
      "}"
    );
  }

  /**
   * Returns true or false depending on whether the given object is equal to this object.
   * @param o the object to compare
   * @return true if the given object is equal to this object
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof TeamMembership)) {
      return false;
    }
    TeamMembership teamMemberships = (TeamMembership) o;
    return (
      Objects.equals(id, teamMemberships.id) &&
      Objects.equals(userId, teamMemberships.userId) &&
      Objects.equals(teamId, teamMemberships.teamId) &&
      Objects.equals(role, teamMemberships.role)
    );
  }
}
