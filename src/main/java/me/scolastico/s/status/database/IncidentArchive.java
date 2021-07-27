package me.scolastico.s.status.database;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import javax.persistence.Entity;

@Entity
public class IncidentArchive extends BaseModel {

  @NotNull @Length(100)
  private String checkId;

  @NotNull
  private long createdAt;

  @NotNull
  private long endedAt;

  @NotNull @Length(1000)
  private String reason;

  @NotNull @Length(1)
  private int status;

  public IncidentArchive(String checkId, long createdAt, long endedAt, String reason, int status) {
    this.checkId = checkId;
    this.createdAt = createdAt;
    this.endedAt = endedAt;
    this.reason = reason;
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

  public long getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(long endedAt) {
    this.endedAt = endedAt;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
