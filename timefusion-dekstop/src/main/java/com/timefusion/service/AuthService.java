package com.timefusion.service;

import com.timefusion.dao.UserDao;
import com.timefusion.exception.AuthenticationException;
import com.timefusion.model.User;
import com.timefusion.util.EncryptionUtil;

public class AuthService {

  private UserDao userDao;

  public AuthService(UserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * Authenticate a user with their email and password.
   *
   * @param email The user's email address.
   * @param password The user's password.
   * @return The id of the authenticated user.
   * @throws AuthenticationException if authentication fails.
   */
  public User authenticate(String email, String password)
    throws AuthenticationException {
    try {
      User user = this.userDao.findByEmail(email);
      System.out.println("User found: " + user);

      if (user != null) {
        if (EncryptionUtil.verifyPassword(password, user.getPassword())) {
          return user;
        } else {
          System.out.println("Incorrect password");
          throw new AuthenticationException("Incorrect password");
        }
      } else {
        throw new AuthenticationException("User not found");
      }
    } catch (AuthenticationException e) {
      throw new AuthenticationException("Error authenticating user", e);
    }
  }
}
