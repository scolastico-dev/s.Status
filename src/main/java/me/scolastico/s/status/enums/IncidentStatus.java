package me.scolastico.s.status.enums;

public enum IncidentStatus {
  DETECTED_ISSUE(1),
  SEARCHING_FOR_ISSUE(2),
  FOUND_ISSUE(3),
  WORKING_ON_ISSUE(4),
  SEEMS_TO_BE_ONLINE_AGAIN(5),
  FIXED_ISSUE(6);

  public final int code;

  private IncidentStatus(int code) {
    this.code = code;
  }
}
