package com.timefusion.localStorage.Entities;

import com.google.gson.JsonObject;

public interface JsonEntity {
  /**
   * Converts the entity to a JSON string.
   *
   * @return The JSON string representation of the entity.
   */
  String toJson();

  /**
   * Converts the entity to a JsonObject.
   *
   * @return The JsonObject representation of the entity.
   */
  JsonObject toJsonObject();

  /**
   * Returns a string representation of the entity.
   *
   * @return The string representation of the entity.
   */
  String toString();

  /**
   * Indicates whether some other object is "equal to" this one.
   * The equality is determined based on the content of the object.
   *
   * @param o the reference object with which to compare.
   * @return true if this object is the same as the o argument; false otherwise.
   */
  boolean equals(Object o);
}
