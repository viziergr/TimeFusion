package com.timefusion.sync;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkStateManager {

  private static boolean hasWifiConnection = true;

  public static boolean hasWifiConnection() {
    return hasWifiConnection;
  }

  public static void sethasWifiConnection(boolean hasWifiConnection) {
    NetworkStateManager.hasWifiConnection = hasWifiConnection;
  }

  public static void detectWifiState() {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();

        if (
          networkInterface.getName().startsWith("wlan") ||
          networkInterface.getName().startsWith("Wi-Fi")
        ) {
          Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

          while (inetAddresses.hasMoreElements()) {
            InetAddress address = inetAddresses.nextElement();

            if (
              !address.isLoopbackAddress() &&
              address.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")
            ) {
              sethasWifiConnection(true);
              return;
            }
          }
        }
      }
      sethasWifiConnection(false);
    } catch (SocketException e) {
      e.printStackTrace();
      sethasWifiConnection(false);
    }
  }
}
