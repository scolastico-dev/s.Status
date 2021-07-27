package me.scolastico.s.status.database;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import javax.persistence.Entity;

@Entity
public class PlannedMaintenance extends BaseModel {

  @NotNull
  @Length(100)
  private String checkId;

  @NotNull
  private long startingAt;

  @NotNull
  private long endingAt;

  @NotNull @Length(1000)
  private String reason;

  public PlannedMaintenance(String checkId, long startingAt, long endingAt, String reason) {
    this.checkId = checkId;
    this.startingAt = startingAt;
    this.endingAt = endingAt;
    this.reason = reason;
  }

  public String getCheckId() {
    return checkId;
  }

  public void setCheckId(String checkId) {
    this.checkId = checkId;
  }

  public long getStartingAt() {
    return startingAt;
  }

  public void setStartingAt(long startingAt) {
    this.startingAt = startingAt;
  }

  public long getEndingAt() {
    return endingAt;
  }

  public void setEndingAt(long endingAt) {
    this.endingAt = endingAt;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}
