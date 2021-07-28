package me.scolastico.s.status.webserver;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.IncidentArchive;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.s.status.dataholders.StatusCheck;
import me.scolastico.s.status.internal.etc.TwigRenderer;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class StatusPage implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(context = {"/", "/index", "/index.html", "/index.php"})
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    Config config = Application.getConfig();
    Map<String, Object> map = new HashMap<>();
    HashMap<String, IncidentArchive> incidents = new HashMap<>();
    try {
      for (StatusCheck check:config.getStatusChecks()) {
        incidents.put(check.getId(), new QIncidentArchive().checkId.eq(check.getId()).orderBy().id.desc().setMaxRows(1).findOne());
      }
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    HashMap<String, String> languages = new HashMap<>();
    for (String id:Application.getLanguage().getLanguages().keySet()) {
      languages.put(id, Application.getLanguage().getLanguages().get(id).getLanguageName());
    }
    if (languages.size() > 1) {
      map.put("languages", languages);
    }
    map.put("pageButtons", config.getPageButtons());
    map.put("statusChecks", config.getStatusChecks());
    map.put("nextUpdateIn", config.getLiveUpdateInterval());
    map.put("incidents", incidents);
    return new Pair<>(200, TwigRenderer.renderTwigTemplate(HTTP_EXCHANGE, "index.html.twig", map));
  }

}
