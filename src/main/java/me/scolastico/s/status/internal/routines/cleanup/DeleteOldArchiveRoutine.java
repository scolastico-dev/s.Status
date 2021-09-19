package me.scolastico.s.status.internal.routines.cleanup;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.query.QIncidentArchive;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class DeleteOldArchiveRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    Config config = Application.getConfig();
    long currentTime = System.currentTimeMillis()/1000;
    new QIncidentArchive()
        .createdAt
        .lessThan(currentTime-(config.getKeepArchiveForDays()*24L*60L*60L))
        .delete();
    return new RoutineAnswer(objectMap);
  }

}
