package me.scolastico.s.status.dataholders;

import java.util.HashMap;
import me.scolastico.s.status.enums.ApiUsability;
import me.scolastico.s.status.enums.CheckTypes;
import me.scolastico.tools.ebean.DatabaseConfig;

public class Config {

  private int port = 42001;
  private String twigFolderPath = "web/default-web-files/twig/";
  private String staticFolderPath = "web/default-web-files/web/";
  private DatabaseConfig databaseConfig = new DatabaseConfig();
  private boolean reportErrorsOnline = true;
  private boolean debug = false;
  private boolean enableShutDownHook = true;
  private boolean enableErrorLogFile = true;
  private boolean showWebServerLog = true;
  private int liveUpdateInterval = 30;
  private int timeoutMillis = 10000;
  private int keepResultsForDays = 7;
  private int keepArchiveForDays = 30;
  private int needsToBeAliveForMinutesBeforeAutoConfirmation = 60;
  private int runCleanUpEveryMinutes = 5;
  private ApiUsability allowGetApiUse = ApiUsability.ALLOW_WITH_API_TOKEN;
  private ApiUsability allowPublicPage = ApiUsability.ALLOW;
  private HashMap<Integer, String> responseTimeColor = new HashMap<>(){{put(0, "#18db4c"); put(500, "#f5ef42"); put(1000, "#e30e0e");}};
  private HashMap<Integer, String> uptimeColor = new HashMap<>(){{put(100, "#18db4c"); put(98, "#e30e0e"); put(90, "#e30e0e");}};
  private HashMap<String, String> pageButtons = new HashMap<>(){{put("about", "https://example.com/");}};
  private StatusCheck[] statusChecks = {new StatusCheck(), new StatusCheck("8.8.8.8", CheckTypes.PING, "Google DNS server (entire not service)", true, "google-dns", 60)};

  public String getResponseTimeColorOrDefault(Integer responseTime) {
    String color = "#ffffff";
    int lastTime = -1;
    for (Integer minTime:responseTimeColor.keySet()) {
      if (responseTime >= minTime && minTime > lastTime) {
        lastTime = minTime;
        color = responseTimeColor.get(minTime);
      }
    }
    return color;
  }

  public String getUptimeColorOrDefault(double uptimePercent) {
    String color = "#ffffff";
    double lastTime = 101.0;
    for (Integer percent:uptimeColor.keySet()) {
      if (uptimePercent <= percent && percent < lastTime) {
        lastTime = percent;
        color = uptimeColor.get(percent);
      }
    }
    return color;
  }

  public DatabaseConfig getDatabaseConfig() {
    return databaseConfig;
  }

  public void setDatabaseConfig(DatabaseConfig databaseConfig) {
    this.databaseConfig = databaseConfig;
  }

  public int getRunCleanUpEveryMinutes() {
    return runCleanUpEveryMinutes;
  }

  public void setRunCleanUpEveryMinutes(int runCleanUpEveryMinutes) {
    this.runCleanUpEveryMinutes = runCleanUpEveryMinutes;
  }

  public boolean isEnableShutDownHook() {
    return enableShutDownHook;
  }

  public void setEnableShutDownHook(boolean enableShutDownHook) {
    this.enableShutDownHook = enableShutDownHook;
  }

  public HashMap<Integer, String> getUptimeColor() {
    return uptimeColor;
  }

  public void setUptimeColor(HashMap<Integer, String> uptimeColor) {
    this.uptimeColor = uptimeColor;
  }

  public HashMap<Integer, String> getResponseTimeColor() {
    return responseTimeColor;
  }

  public void setResponseTimeColor(HashMap<Integer, String> responseTimeColor) {
    this.responseTimeColor = responseTimeColor;
  }

  public int getLiveUpdateInterval() {
    return liveUpdateInterval;
  }

  public void setLiveUpdateInterval(int liveUpdateInterval) {
    this.liveUpdateInterval = liveUpdateInterval;
  }

  public boolean isShowWebServerLog() {
    return showWebServerLog;
  }

  public void setShowWebServerLog(boolean showWebServerLog) {
    this.showWebServerLog = showWebServerLog;
  }

  public HashMap<String, String> getPageButtons() {
    return pageButtons;
  }

  public void setPageButtons(HashMap<String, String> pageButtons) {
    this.pageButtons = pageButtons;
  }

  public String getTwigFolderPath() {
    return twigFolderPath;
  }

  public void setTwigFolderPath(String twigFolderPath) {
    this.twigFolderPath = twigFolderPath;
  }

  public String getStaticFolderPath() {
    return staticFolderPath;
  }

  public void setStaticFolderPath(String staticFolderPath) {
    this.staticFolderPath = staticFolderPath;
  }

  public int getTimeoutMillis() {
    return timeoutMillis;
  }

  public void setTimeoutMillis(int timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public int getNeedsToBeAliveForMinutesBeforeAutoConfirmation() {
    return needsToBeAliveForMinutesBeforeAutoConfirmation;
  }

  public void setNeedsToBeAliveForMinutesBeforeAutoConfirmation(int needsToBeAliveForMinutesBeforeAutoConfirmation) {
    this.needsToBeAliveForMinutesBeforeAutoConfirmation = needsToBeAliveForMinutesBeforeAutoConfirmation;
  }

  public boolean isEnableErrorLogFile() {
    return enableErrorLogFile;
  }

  public void setEnableErrorLogFile(boolean enableErrorLogFile) {
    this.enableErrorLogFile = enableErrorLogFile;
  }

  public int getKeepResultsForDays() {
    return keepResultsForDays;
  }

  public void setKeepResultsForDays(int keepResultsForDays) {
    this.keepResultsForDays = keepResultsForDays;
  }

  public int getKeepArchiveForDays() {
    return keepArchiveForDays;
  }

  public void setKeepArchiveForDays(int keepArchiveForDays) {
    this.keepArchiveForDays = keepArchiveForDays;
  }

  public ApiUsability getAllowGetApiUse() {
    return allowGetApiUse;
  }

  public void setAllowGetApiUse(ApiUsability allowGetApiUse) {
    this.allowGetApiUse = allowGetApiUse;
  }

  public ApiUsability getAllowPublicPage() {
    return allowPublicPage;
  }

  public void setAllowPublicPage(ApiUsability allowPublicPage) {
    this.allowPublicPage = allowPublicPage;
  }

  public StatusCheck[] getStatusChecks() {
    return statusChecks;
  }

  public void setStatusChecks(StatusCheck[] statusChecks) {
    this.statusChecks = statusChecks;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isReportErrorsOnline() {
    return reportErrorsOnline;
  }

  public void setReportErrorsOnline(boolean reportErrorsOnline) {
    this.reportErrorsOnline = reportErrorsOnline;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

}
