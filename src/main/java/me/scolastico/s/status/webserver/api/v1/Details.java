package me.scolastico.s.status.webserver.api.v1;

import com.sun.net.httpserver.HttpExchange;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.IncidentArchive;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.database.query.QStatusCheckResult;
import me.scolastico.s.status.dataholders.PageInfoCardDataHolder;
import me.scolastico.s.status.dataholders.StatusCheck;
import me.scolastico.s.status.enums.IncidentStatus;
import me.scolastico.s.status.internal.etc.TwigRenderer;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.CookieManager;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class Details implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(context = "/api/v1/details/~", acceptPathValues = true, isAPI = true, apiDescription = "API for async loading of details for the normal status page. Will generate HTML.", usageWeight = 5)
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    try {
      Map<String, Object> map = new HashMap<>();
      String id = PATH_VALUES[0];
      for (StatusCheck check:Application.getConfig().getStatusChecks()) {
        if (check.getId().equals(id)) {
          map.put("id", check.getId());
          String language = CookieManager.getCookie(HTTP_EXCHANGE, "language");
          if (language == null) language = Application.getLanguage().getDefaultLanguage();
          ArrayList<PageInfoCardDataHolder> infoCards = new ArrayList<>();
          List<IncidentArchive> archive = new QIncidentArchive().checkId.eq(check.getId()).findList();
          if (archive.size() > 0 && archive.get(archive.size() - 1).getStatus() != IncidentStatus.FIXED_ISSUE.code) {
            infoCards.add(
                new PageInfoCardDataHolder(
                    Application.getLanguage()
                        .getLanguage(language)
                        .getIncidentInfoCard(),
                    true));
          }
          HashMap<Integer, Double> uptimePercent = new HashMap<>();
          for (int i = 0; i < Application.getConfig().getKeepArchiveForDays(); i++) {
            uptimePercent.put(i, 100.0);
          }
          long currentTime = System.currentTimeMillis()/1000;
          for (IncidentArchive incident:archive) {
            int daysBetween = 0;
            long startedAt = incident.getCreatedAt();
            long endedAt = incident.getEndedAt();
            if (endedAt == 0L) endedAt = currentTime;
            int startDaysInThePast = (int) ((currentTime-startedAt)/86400L);
            long dayEnding = getDayEnding(startedAt);
            while (endedAt > dayEnding) {
              daysBetween++;
              dayEnding+=86400;
            }
            if (daysBetween > 1) {
              for (int i = 1; i < daysBetween; i++) {
                uptimePercent.remove(startDaysInThePast-i);
                uptimePercent.put(startDaysInThePast-i, 0.0);
              }
            }
            double duration;
            double currentPercent = uptimePercent.get(startDaysInThePast);
            uptimePercent.remove(startDaysInThePast);
            if (daysBetween > 0) {
              duration = (double) (getDayEnding(startedAt) - startedAt);
              uptimePercent.put(startDaysInThePast, currentPercent-((100D*duration)/86400D));
              duration = (double)(endedAt - getDayBeginning(endedAt));
              currentPercent = uptimePercent.get(startDaysInThePast-daysBetween);
              uptimePercent.remove(startDaysInThePast-daysBetween);
              uptimePercent.put(startDaysInThePast-daysBetween, currentPercent-((100D*duration)/86400D));
            } else {
              duration = (double) (endedAt - startedAt);
              uptimePercent.put(startDaysInThePast, currentPercent-((100D*duration)/86400D));
            }
          }
          HashMap<Integer, Double> uptimePercentClone = new HashMap<>();
          for(int i:uptimePercent.keySet()){
            uptimePercentClone.put(i, round(uptimePercent.get(i), 2));
          }
          map.put("uptimePercent", uptimePercentClone);
          if (archive.size() > 0) map.put("incidents", archive);
          map.put("config", Application.getConfig());
          map.put("checks", new QStatusCheckResult().checkId.eq(check.getId()).findList());
          map.put("infos", infoCards.toArray(new PageInfoCardDataHolder[0]));
          return new Pair<>(200, TwigRenderer.renderTwigTemplate(HTTP_EXCHANGE, "details.html.twig", map));
        }
      }
    } catch (Exception e) {
      ErrorHandler.handle(e);
      return new Pair<>(500, "{\"status\":\"error\",\"error\":\"internal server error\"}");
    }
    return new Pair<>(404, "{\"status\":\"error\",\"error\":\"not found\"}");
  }

  private static long getDayBeginning(long time) {
    time = time*1000L;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(time));
    calendar.set(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    );
    return calendar.getTimeInMillis()/1000;
  }

  private static long getDayEnding(long time) {
    return getDayBeginning(time)+86399;
  }

  private static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();
    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
