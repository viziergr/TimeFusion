package com.timefusion.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This class provides utility methods for encrypting and verifying passwords using BCrypt.
 * BCrypt is a strong hashing algorithm that is resistant to rainbow table attacks.
 * It uses a configurable workload factor to control the time taken to hash a password,
 * making it computationally expensive for attackers to crack the hashed passwords.
 */
public class EncryptionUtil {

  /**
   * Hashing function for passwords using BCrypt.
   *
   * @param password The password to hash.
   * @return The hashed password.
   */
  public static String hashPassword(String password) {
    // Set the BCrypt workload factor
    int workload = 12;

    // Return hashed password
    return BCrypt.hashpw(password, BCrypt.gensalt(workload));
  }

  /**
   * Verify a password against its BCrypt hash.
   *
   * @param password       The password to check.
   * @param hashedPassword The hashed password to compare against.
   * @return true if the password matches the hashedPassword, false otherwise.
   */
  public static boolean verifyPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, migratePassword(hashedPassword));
  }

  private static String migratePassword(String hashedPasswordWithOldPrefix) {
    if (hashedPasswordWithOldPrefix.startsWith("$2a$")) {
      return hashedPasswordWithOldPrefix; // No need to migrate, already using $2a
    }

    String hashedPasswordWithNewPrefix = hashedPasswordWithOldPrefix.replaceFirst(
      "\\$2y\\$",
      "\\$2a\\$"
    );

    return hashedPasswordWithNewPrefix;
  }
}
