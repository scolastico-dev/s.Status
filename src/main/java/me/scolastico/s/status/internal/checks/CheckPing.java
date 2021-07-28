package me.scolastico.s.status.internal.checks;

import java.net.InetAddress;

public class CheckPing {

  public static int checkStatus(String host) {
    try {
      return InetAddress.getByName(host).isReachable(10000) ? 200 : 408;
    } catch (Exception ignored) {}
    return 1;
  }

}
