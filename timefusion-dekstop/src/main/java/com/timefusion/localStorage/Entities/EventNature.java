package com.timefusion.localStorage.Entities;

public enum EventNature {
  ADDED("Added"),
  UPDATED("Updated"),
  DELETED("Deleted"),
  DENIED("Denied"),
  INVITED("Invited"),
  UNCHANGED("Unchanged");

  private final String displayName;

  EventNature(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
