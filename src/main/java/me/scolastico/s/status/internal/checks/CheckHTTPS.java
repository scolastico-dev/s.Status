package me.scolastico.s.status.internal.checks;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import me.scolastico.s.status.Application;

public class CheckHTTPS {

  public static int checkStatus(String host) {
    try {
      URL url = new URL(host);
      HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setConnectTimeout(Application.getConfig().getTimeoutMillis());
      con.setReadTimeout(Application.getConfig().getTimeoutMillis());
      return con.getResponseCode();
    } catch (Exception ignored) {}
    return 1;
  }

}
