package me.scolastico.s.status.internal.routines.starting;

import java.io.File;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class CopyDefaultFilesRoutine  implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      File webFolder = new File("web");
      if (!webFolder.exists() || !webFolder.isDirectory()) {
        System.out.print(Ansi.ansi().a("Copying default files... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        Application.writeDefaultFiles(false);
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
      }
    } catch (Exception e) {
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ConsoleLoadingAnimation.disable();
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
