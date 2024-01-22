package com.timefusion.util;

//A utiliser au détriment de ApplicationConfig.java

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

  private static final String PROP_FILE = "/config.properties";
  private static Properties properties;

  static {
    try (InputStream input = ConfigUtil.class.getResourceAsStream(PROP_FILE)) {
      properties = new Properties();
      properties.load(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
}
