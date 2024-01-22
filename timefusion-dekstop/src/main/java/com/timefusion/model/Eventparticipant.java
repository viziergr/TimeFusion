package com.timefusion.model;

import java.util.Objects;

public class EventParticipant {

  private int id;
  private int participantId;
  private int eventId;

  /**
   * Constructor for Eventparticipant
   * @param id
   * @param participantId
   * @param eventId
   */
  public EventParticipant(int id, int participantId, int eventId) {
    this.id = id;
    this.participantId = participantId;
    this.eventId = eventId;
  }

  /**
   * Constructor for Eventparticipant
   * @param participantId
   * @param eventId
   */
  public EventParticipant(int participantId, int eventId) {
    this(0, participantId, eventId);
  }

  /**
   * Returns the ID of the event participant.
   * @return the ID of the event participant
   */
  public int getId() {
    return this.id;
  }

  /**
   * Returns the ID of the user.
   * @return the ID of the user
   */
  public int getParticipantId() {
    return this.participantId;
  }

  /**
   * Returns the ID of the event.
   * @return the ID of the event
   */
  public int getEventId() {
    return this.eventId;
  }

  /**
   * Sets the ID of the event participant.
   * @param id the ID of the event participant
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the ID of the user.
   * @param participantId the ID of the user
   */
  public void setParticipantId(int participantId) {
    this.participantId = participantId;
  }

  /**
   * Sets the ID of the event.
   * @param eventId the ID of the event
   */
  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  /**
   * Compares this Eventparticipant object with the specified object for equality.
   * Returns true if the given object is also an Eventparticipant and has the same values for id, participantId, eventId, and participant_type.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof EventParticipant)) {
      return false;
    }
    EventParticipant eventparticipant = (EventParticipant) o;
    return (
      Objects.equals(id, eventparticipant.id) &&
      Objects.equals(participantId, eventparticipant.participantId) &&
      Objects.equals(eventId, eventparticipant.eventId)
    );
  }

  /**
   * Returns a string representation of the Eventparticipant.
   * @return a string representation of the Eventparticipant
   */
  @Override
  public String toString() {
    return (
      "Eventparticipant{" +
      "id=" +
      id +
      ", participantId=" +
      participantId +
      ", eventId=" +
      eventId +
      '}'
    );
  }
}
