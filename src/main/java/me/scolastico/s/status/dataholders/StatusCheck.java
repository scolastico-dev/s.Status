package me.scolastico.s.status.dataholders;

import me.scolastico.s.status.enums.CheckTypes;

public class StatusCheck {

  private String host = "https://google.com/";
  private CheckTypes type = CheckTypes.HTTPS;
  private String name = "Google";
  private boolean hideHostOnPublicPage = true;
  private String id = "google";
  private int checkEverySeconds = 60;

  public StatusCheck() {}

  public StatusCheck(String host, CheckTypes type, String name, boolean hideHostOnPublicPage, String id, int checkEverySeconds) {
    this.host = host;
    this.type = type;
    this.name = name;
    this.hideHostOnPublicPage = hideHostOnPublicPage;
    this.id = id;
    this.checkEverySeconds = checkEverySeconds;
  }

  public int getCheckEverySeconds() {
    return checkEverySeconds;
  }

  public void setCheckEverySeconds(int checkEverySeconds) {
    this.checkEverySeconds = checkEverySeconds;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public CheckTypes getType() {
    return type;
  }

  public void setType(CheckTypes type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isHideHostOnPublicPage() {
    return hideHostOnPublicPage;
  }

  public void setHideHostOnPublicPage(boolean hideHostOnPublicPage) {
    this.hideHostOnPublicPage = hideHostOnPublicPage;
  }

}
