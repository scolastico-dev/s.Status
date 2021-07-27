package me.scolastico.s.status.dataholders;

import java.util.HashMap;

public class Language {

  private String languageName = "English";
  private String pageTitle = "s.Status";
  private String pageDescription = "Are our servers all online and accessible? Find out here with live updates and notifications if you're waiting for a service to come back online.";
  private String nextUpdateIn = "Next update in %seconds% seconds.";
  private String incidents = "Incidents";
  private String startedAt = "Started";
  private String endedAt = "Ended";
  private String reason = "Reason";
  private String status = "Status";
  private String detectedIssue = "Detected Issues";
  private String searchingForIssue = "Searching For Issues";
  private String foundIssue = "Found Issues";
  private String workingOnIssue = "Working On Issues";
  private String seemsToBeOnlineAgain = "Seems To Be Online Again";
  private String fixedIssue = "Fixed Issues";
  private String noIncidentsFound = "Seems like there where no incidents in the last 30 days! Yay!";
  private String incidentInfoCard = "It seems like this service has currently some problems! For more information scroll down to the incident list.";
  private String responseTime = "Response time in ms";
  private String onlinePerDay = "Online per day in %";
  private String unknownErrorTitle = "Unknown Error...";
  private String unknownError = "An unknown server error occurred. Please try again later.";
  private String toManyRequestsTitle = "Too many requests...";
  private String toManyRequests = "Unfortunately you have made too many inquiries. Please wait a moment before trying again.";

  private HashMap<Integer, String> incidentDescription = new HashMap<>(){{
    put(1, "Detected Issues...");
    put(2, "Our team is searching for the issue...");
    put(3, "We found the issue!");
    put(4, "We are currently implementing a fix!");
    put(5, "Seems to be online again! Further testing is required!");
    put(6, "We fixed the issue!");
  }};

  private HashMap<String, String> additionalLanguageData = new HashMap<>(){{
    put("about", "About");
  }};

  public String getUnknownErrorTitle() {
    return unknownErrorTitle;
  }

  public void setUnknownErrorTitle(String unknownErrorTitle) {
    this.unknownErrorTitle = unknownErrorTitle;
  }

  public String getUnknownError() {
    return unknownError;
  }

  public void setUnknownError(String unknownError) {
    this.unknownError = unknownError;
  }

  public String getToManyRequestsTitle() {
    return toManyRequestsTitle;
  }

  public void setToManyRequestsTitle(String toManyRequestsTitle) {
    this.toManyRequestsTitle = toManyRequestsTitle;
  }

  public String getToManyRequests() {
    return toManyRequests;
  }

  public void setToManyRequests(String toManyRequests) {
    this.toManyRequests = toManyRequests;
  }

  public String getLanguageName() {
    return languageName;
  }

  public void setLanguageName(String languageName) {
    this.languageName = languageName;
  }

  public String getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(String responseTime) {
    this.responseTime = responseTime;
  }

  public String getOnlinePerDay() {
    return onlinePerDay;
  }

  public void setOnlinePerDay(String onlinePerDay) {
    this.onlinePerDay = onlinePerDay;
  }

  public String getIncidentInfoCard() {
    return incidentInfoCard;
  }

  public void setIncidentInfoCard(String incidentInfoCard) {
    this.incidentInfoCard = incidentInfoCard;
  }

  public HashMap<Integer, String> getIncidentDescription() {
    return incidentDescription;
  }

  public void setIncidentDescription(HashMap<Integer, String> incidentDescription) {
    this.incidentDescription = incidentDescription;
  }

  public String getNoIncidentsFound() {
    return noIncidentsFound;
  }

  public void setNoIncidentsFound(String noIncidentsFound) {
    this.noIncidentsFound = noIncidentsFound;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public String getPageDescription() {
    return pageDescription;
  }

  public void setPageDescription(String pageDescription) {
    this.pageDescription = pageDescription;
  }

  public String getNextUpdateIn() {
    return nextUpdateIn;
  }

  public void setNextUpdateIn(String nextUpdateIn) {
    this.nextUpdateIn = nextUpdateIn;
  }

  public String getIncidents() {
    return incidents;
  }

  public void setIncidents(String incidents) {
    this.incidents = incidents;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(String startedAt) {
    this.startedAt = startedAt;
  }

  public String getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(String endedAt) {
    this.endedAt = endedAt;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDetectedIssue() {
    return detectedIssue;
  }

  public void setDetectedIssue(String detectedIssue) {
    this.detectedIssue = detectedIssue;
  }

  public String getSearchingForIssue() {
    return searchingForIssue;
  }

  public void setSearchingForIssue(String searchingForIssue) {
    this.searchingForIssue = searchingForIssue;
  }

  public String getFoundIssue() {
    return foundIssue;
  }

  public void setFoundIssue(String foundIssue) {
    this.foundIssue = foundIssue;
  }

  public String getWorkingOnIssue() {
    return workingOnIssue;
  }

  public void setWorkingOnIssue(String workingOnIssue) {
    this.workingOnIssue = workingOnIssue;
  }

  public String getSeemsToBeOnlineAgain() {
    return seemsToBeOnlineAgain;
  }

  public void setSeemsToBeOnlineAgain(String seemsToBeOnlineAgain) {
    this.seemsToBeOnlineAgain = seemsToBeOnlineAgain;
  }

  public String getFixedIssue() {
    return fixedIssue;
  }

  public void setFixedIssue(String fixedIssue) {
    this.fixedIssue = fixedIssue;
  }

  public HashMap<String, String> getAdditionalLanguageData() {
    return additionalLanguageData;
  }

  public void setAdditionalLanguageData(HashMap<String, String> additionalLanguageData) {
    this.additionalLanguageData = additionalLanguageData;
  }

}
