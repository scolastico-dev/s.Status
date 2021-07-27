package me.scolastico.s.status.internal.etc;

import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.database.query.QStatusCheckResult;
import me.scolastico.s.status.dataholders.Config;

public class CleanUpScheduler {

  public static void run() throws Exception {
    Config config = Application.getConfig();
    long currentTime = System.currentTimeMillis()/1000;
    new QIncidentArchive()
        .createdAt
        .lessThan(currentTime-(config.getKeepArchiveForDays()*24L*60L*60L))
        .delete();
    new QStatusCheckResult()
        .createdAt
        .lessThan(currentTime-(config.getKeepArchiveForDays()*24L*60L*60L))
        .delete();
    new QStatusCheckResult()
        .checkDuration
        .greaterThan(config.getTimeoutMillis())
        .asUpdate()
        .set("checkDuration", config.getTimeoutMillis())
        .update();
  }

}
