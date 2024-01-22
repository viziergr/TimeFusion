package com.timefusion.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating user input.
 */
public final class ValidationUtil {

  private static final int MAX_EMAIL_LENGTH = 75;
  private static final int MIN_EMAIL_LENGTH = 5;
  private static final int MAX_PASSWORD_LENGTH = 20;
  private static final int MIN_PASSWORD_LENGTH = 8;

  private static final String EMAIL_REGEX =
    "^(?=.{" +
    MIN_EMAIL_LENGTH +
    "," +
    MAX_EMAIL_LENGTH +
    "}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,5}$";

  private static final String PASSWORD_REGEX =
    "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{" +
    MIN_PASSWORD_LENGTH +
    "," +
    MAX_PASSWORD_LENGTH +
    "}$";

  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
    PASSWORD_REGEX
  );

  private ValidationUtil() {
    // Private constructor to prevent instantiation
  }

  /**
   * Validate an email address.
   *
   * @param email The email address to validate.
   * @return true if the email is valid, false otherwise
   */
  public static boolean isValidEmail(String email) {
    return email != null && EMAIL_PATTERN.matcher(email).matches();
  }

  /**
   * Validate a password.
   *
   * @param password The password to validate.
   * @return true if the password is valid, false otherwise
   */
  public static boolean isValidPassword(String password) {
    return password != null && PASSWORD_PATTERN.matcher(password).matches();
  }
}
