package me.scolastico.s.status.webserver.api.v1;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.s.status.Application;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;
import org.json.JSONObject;

public class Status implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(context = "/api/v1/status", isAPI = true, apiDescription = "Status page which shows information about the installation like version.", usageWeight = 1)
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    HTTP_EXCHANGE.getResponseHeaders().add("Content-Type", "application/json");
    JSONObject object = new JSONObject();
    object.put("status", "ok");
    object.put("version", Application.getVersion());
    object.put("commit", Application.getCommit());
    object.put("branch", Application.getBranch());
    object.put("languages", Application.getLanguage().getLanguages().keySet());
    object.put("ts", System.currentTimeMillis()/1000);
    return new Pair<>(200, object.toString());
  }

}
