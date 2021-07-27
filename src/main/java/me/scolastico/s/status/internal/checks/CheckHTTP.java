package me.scolastico.s.status.internal.checks;

import java.net.HttpURLConnection;
import java.net.URL;
import me.scolastico.s.status.Application;

public class CheckHTTP {

  public static int checkStatus(String host) {
    try {
      URL url = new URL(host);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setConnectTimeout(Application.getConfig().getTimeoutMillis());
      con.setReadTimeout(Application.getConfig().getTimeoutMillis());
      return con.getResponseCode();
    } catch (Exception ignored) {}
    return 1;
  }

}
