package me.scolastico.s.status.internal.routines.starting;

import com.github.lalyos.jfiglet.FigletFont;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.AnsiConsole;

public class HeaderRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    objectMap.put("startingTime", System.currentTimeMillis());
    AnsiConsole.systemInstall();
    ErrorHandler.enableCatchUncaughtException();

    System.out.println(FigletFont.convertOneLine("s.Status"));
    System.out.println(
        "Version: "
            + Application.getVersion()
            + " | Commit: "
            + Application.getBranch()
            + "/"
            + Application.getCommit()
            + " | By: scolastico");
    System.out.println();

    ConsoleLoadingAnimation.setAnimation(new char[]{'-', '\\', '|', '/'});
    ConsoleLoadingAnimation.setSpeed(25);
    return new RoutineAnswer(objectMap);
  }

}
