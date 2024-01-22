package com.timefusion.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an event in the application.
 */
public class Event {

  private int id;
  private String title;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String location;
  private String description;
  private Boolean isPrivate;
  private int creatorId;

  /**
   * Constructor for Event
   * @param id
   * @param title
   * @param startTime
   * @param endTime
   * @param location
   * @param description
   * @param isPrivate
   * @param creatorId
   */
  public Event(
    int id,
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String location,
    String description,
    Boolean isPrivate,
    int creatorId
  ) {
    this.id = id;
    this.title = title;
    this.startTime = startTime;
    this.endTime = endTime;
    this.location = location;
    this.description = description;
    this.isPrivate = isPrivate;
    this.creatorId = creatorId;
  }

  /**
   * Constructor for Event
   * @param title
   * @param startTime
   * @param endTime
   * @param description
   * @param isPrivate
   * @param creatorId
   */
  public Event(
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String location,
    String description,
    Boolean isPrivate,
    int creatorId
  ) {
    this(
      0,
      title,
      startTime,
      endTime,
      location,
      description,
      isPrivate,
      creatorId
    );
  }

  /**
   * Constructor for Event
   * @param title
   * @param startTime
   * @param endTime
   * @param description
   * @param isPrivate
   * @param creatorId
   */
  public Event(
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String description,
    Boolean isPrivate,
    int creatorId
  ) {
    this(0, title, startTime, endTime, null, description, isPrivate, creatorId);
  }

  /**
   * Constructor for Event
   * @param title
   * @param startTime
   * @param endTime
   * @param isPrivate
   * @param creatorId
   */
  public Event(
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Boolean isPrivate,
    int creatorId
  ) {
    this(0, title, startTime, endTime, null, null, isPrivate, creatorId);
  }

  /**
   * Constructor for Event
   * @param title
   * @param startTime
   * @param endTime
   * @param location
   * @param isPrivate
   * @param creatorId
   */
  public Event(
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    int creatorId,
    String location,
    Boolean isPrivate
  ) {
    this(0, title, startTime, endTime, location, null, isPrivate, creatorId);
  }

  /**
   * Returns the ID of the event.
   *
   * @return the ID of the event
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the ID of the event.
   *
   * @param id the ID of the event
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the title of the event.
   *
   * @return the title of the event
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the event.
   *
   * @param title the title of the event
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the start time of the event.
   *
   * @return the start time of the event
   */
  public LocalDateTime getStartTime() {
    return this.startTime;
  }

  /**
   * Sets the start time of the event.
   *
   * @param startTime the start time of the event
   */
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  /**
   * Returns the end time of the event.
   *
   * @return the end time of the event
   */
  public LocalDateTime getEndTime() {
    return this.endTime;
  }

  /**
   * Sets the end time of the event.
   *
   * @param endTime the end time of the event
   */
  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  /**
   * Returns the location of the event.
   *
   * @return the location of the event
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * Sets the location of the event.
   *
   * @param location the location of the event
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Returns the description of the event.
   *
   * @return the description of the event
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the event.
   *
   * @param description the description of the event
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns whether the event is private or not.
   *
   * @return whether the event is private or not
   */
  public Boolean getIsPrivate() {
    return this.isPrivate;
  }

  /**
   * Sets whether the event is private or not.
   *
   * @param isPersonal whether the event is private or not
   */
  public void setIsPrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  /**
   * Returns the creatorId of the event.
   *
   * @return the creatorId of the event
   */
  public int getCreatorId() {
    return this.creatorId;
  }

  /**
   * Sets the creatorId of the event.
   *
   * @param creatorId the creatorId of the event
   */
  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  /**
   * Returns a string representation of the event.
   *
   * @return a string representation of the event
   */
  @Override
  public String toString() {
    return (
      "Event{" +
      "id=" +
      id +
      ", title='" +
      title +
      ", startTime='" +
      startTime +
      ", endTime='" +
      endTime +
      ", location='" +
      location +
      ", description='" +
      description +
      ", isPrivate='" +
      isPrivate +
      ", creatorId='" +
      creatorId +
      "}"
    );
  }

  /**
   * Returns whether the event is equal to another object.
   *
   * @param o the object to compare to
   * @return whether the event is equal to another object
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return (
      Objects.equals(id, event.id) &&
      Objects.equals(title, event.title) &&
      Objects.equals(startTime, event.startTime) &&
      Objects.equals(endTime, event.endTime) &&
      Objects.equals(location, event.location) &&
      Objects.equals(description, event.description) &&
      Objects.equals(isPrivate, event.isPrivate) &&
      Objects.equals(creatorId, event.creatorId)
    );
  }
}
