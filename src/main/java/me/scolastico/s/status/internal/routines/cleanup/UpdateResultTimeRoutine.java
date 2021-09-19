package me.scolastico.s.status.internal.routines.cleanup;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.database.query.QStatusCheckResult;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class UpdateResultTimeRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    Config config = Application.getConfig();
    new QStatusCheckResult()
        .checkDuration
        .greaterThan(config.getTimeoutMillis())
        .asUpdate()
        .set("checkDuration", config.getTimeoutMillis())
        .update();
    return new RoutineAnswer(objectMap);
  }

}
