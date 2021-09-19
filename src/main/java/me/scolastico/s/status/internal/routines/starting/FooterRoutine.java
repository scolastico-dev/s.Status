package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class FooterRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    Long startingTime = (Long) objectMap.get("startingTime");
    System.out.println();
    System.out.println("Done! Starting took " + ((System.currentTimeMillis()-startingTime)/1000D) + " seconds. Try 'help' for help.");
    System.out.println();
    ConsoleManager.enable(true, true);
    return new RoutineAnswer(objectMap);
  }

}
