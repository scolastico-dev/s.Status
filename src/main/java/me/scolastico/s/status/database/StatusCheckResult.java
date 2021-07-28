package me.scolastico.s.status.database;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import javax.persistence.Entity;

@Entity
public class StatusCheckResult extends BaseModel {

  @NotNull @Length(100)
  private String checkId;

  @NotNull
  private long createdAt;

  @NotNull
  private int checkDuration;

  @NotNull @Length(1)
  private int status;

  public StatusCheckResult(String checkId, long createdAt, int checkDuration, int status) {
    this.checkId = checkId;
    this.createdAt = createdAt;
    this.checkDuration = checkDuration;
    this.status = status;
  }

  public String getCheckId() {
    return checkId;
  }

  public void setCheckId(String checkId) {
    this.checkId = checkId;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public int getCheckDuration() {
    return checkDuration;
  }

  public void setCheckDuration(int checkDuration) {
    this.checkDuration = checkDuration;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
