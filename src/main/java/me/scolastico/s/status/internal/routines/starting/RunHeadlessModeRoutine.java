package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class RunHeadlessModeRoutine implements Routine {


  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      String[] args = (String[]) objectMap.get("args");
      if (args.length > 0) {
        System.out.println();
        System.out.println("Detected headless console client usage!");
        System.out.println("Skipping the rest of the startup process now and executing your request!");
        System.out.println();
        System.exit(ConsoleManager.executeCommand(args));
        return new RoutineAnswer(true, "headless");
      }
    }  catch (Exception e) {
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
