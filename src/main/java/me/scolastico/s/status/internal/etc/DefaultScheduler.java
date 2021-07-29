package me.scolastico.s.status.internal.etc;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.IncidentArchive;
import me.scolastico.s.status.database.StatusCheckResult;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.database.query.QStatusCheckResult;
import me.scolastico.s.status.dataholders.StatusCheck;
import me.scolastico.s.status.enums.IncidentStatus;
import me.scolastico.s.status.internal.checks.CheckHTTP;
import me.scolastico.s.status.internal.checks.CheckHTTPS;
import me.scolastico.s.status.internal.checks.CheckPing;
import me.scolastico.tools.handler.ErrorHandler;

public class DefaultScheduler {

  private static final HashMap<String, Long> lastChecked = new HashMap<>();

  public static void run() throws Exception {
    for (StatusCheck check:Application.getConfig().getStatusChecks()) {
      new Thread(
              new Runnable() {
                @Override
                public void run() {
                  try {
                    if (!lastChecked.containsKey(check.getId())) {
                      StatusCheckResult result = new QStatusCheckResult().checkId.eq(check.getId()).id.desc().setMaxRows(1).findOne();
                      if (result == null) {
                        lastChecked.put(check.getId(), 0L);
                      } else {
                        lastChecked.put(check.getId(), result.getCreatedAt());
                      }
                    }
                    if ((System.currentTimeMillis() / 1000) - lastChecked.get(check.getId())
                        >= check.getCheckEverySeconds()) {
                      lastChecked.remove(check.getId());
                      lastChecked.put(check.getId(), System.currentTimeMillis()/1000);
                      int status;
                      long currentTime = System.currentTimeMillis();
                      switch (check.getType()) {
                        case PING:
                          status = CheckPing.checkStatus(check.getHost());
                          break;
                        case HTTP:
                          status = CheckHTTP.checkStatus(check.getHost());
                          break;
                        case HTTPS:
                          status = CheckHTTPS.checkStatus(check.getHost());
                          break;
                        default:
                          status = 0;
                          break;
                      }
                      if (status == 0) return;
                      int time = (int)(System.currentTimeMillis()-currentTime);
                      int timeout = Application.getConfig().getTimeoutMillis();
                      StatusCheckResult result = new StatusCheckResult(
                          check.getId(),
                          System.currentTimeMillis()/1000,
                          Math.min(time, timeout),
                          status
                      );
                      result.save();
                      IncidentArchive archive = new QIncidentArchive().checkId.eq(check.getId()).id.desc().setMaxRows(1).findOne();
                      if (archive == null || archive.getStatus() == IncidentStatus.FIXED_ISSUE.code) {
                        if (status != 200) {
                          IncidentArchive newEntry =
                              new IncidentArchive(
                                  check.getId(),
                                  System.currentTimeMillis() / 1000,
                                  0L,
                                  "Response code was: " + status,
                                  IncidentStatus.DETECTED_ISSUE.code
                              );
                          newEntry.save();
                        }
                      } else {
                        if (status == 200) {
                          if (archive.getStatus() == IncidentStatus.DETECTED_ISSUE.code) {
                            archive.setStatus(IncidentStatus.SEEMS_TO_BE_ONLINE_AGAIN.code);
                            archive.setEndedAt(System.currentTimeMillis()/1000L);
                            archive.update();
                          } else if (archive.getStatus() == IncidentStatus.SEEMS_TO_BE_ONLINE_AGAIN.code) {
                            long dif = System.currentTimeMillis()/1000L;
                            dif -= Application.getConfig().getNeedsToBeAliveForMinutesBeforeAutoConfirmation()*60L;
                            if (archive.getEndedAt() < dif) {
                              archive.setStatus(IncidentStatus.FIXED_ISSUE.code);
                              archive.update();
                            }
                          }
                        } else {
                          if (archive.getStatus() == IncidentStatus.SEEMS_TO_BE_ONLINE_AGAIN.code) {
                            archive.setStatus(IncidentStatus.DETECTED_ISSUE.code);
                            archive.setEndedAt(0L);
                            archive.update();
                          }
                        }
                      }
                    }
                  } catch (Exception e) {
                    ErrorHandler.handle(e);
                  }
                }
              })
          .start();
    }
  }

}
