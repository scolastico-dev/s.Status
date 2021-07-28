package me.scolastico.s.status.webserver.api.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.database.query.QStatusCheckResult;
import me.scolastico.s.status.dataholders.StatusCheck;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class Get implements SimpleWebsiteInterface {

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  @Override
  @WebServerRegistration(context = "/api/v1/get/?~", acceptPathValues = true, isAPI = true, apiDescription = "Get the current server status.", usageWeight = 5)
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    try {
      if (PATH_VALUES.length == 0) {
        ArrayList<String> checkIds = new ArrayList<>();
        for (StatusCheck check:Application.getConfig().getStatusChecks()) {
          checkIds.add(check.getId());
        }
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("status", "ok");
        ret.put("checkIds", checkIds);
        return new Pair<>(200, gson.toJson(ret));
      } else {
        String name = PATH_VALUES[0];
        for (StatusCheck check:Application.getConfig().getStatusChecks()) {
          if (check.getId().equals(name)) {
            HashMap<String, Object> ret = new HashMap<>();
            ret.put("status", "ok");
            ret.put("checks", new QStatusCheckResult().checkId.eq(check.getId()).findList());
            ret.put("incidents", new QIncidentArchive().checkId.eq(check.getId()).findList());
            return new Pair<>(200, gson.toJson(ret));
          }
        }
        return new Pair<>(404, "{\"status\":\"error\",\"error\":\"not found\"}");
      }
    } catch (Exception e) {
      ErrorHandler.handle(e);
      return new Pair<>(500, "{\"status\":\"error\",\"error\":\"internal server error\"}");
    }
    //return new Pair<>(403, "{\"status\":\"error\",\"error\":\"not allowed\"}");
  }

}
