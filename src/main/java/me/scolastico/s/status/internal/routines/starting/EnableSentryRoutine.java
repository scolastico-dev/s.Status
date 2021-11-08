package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class EnableSentryRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      if (Application.getConfig().isReportErrorsOnline()) {
        System.out.print(Ansi.ansi().a("Enabling sentry error reporter... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        String v = Application.getVersion().equals("dev-snapshot") ? Application.getCommit() : Application.getVersion();
        if (!v.equals("IN-DEV")) {
          ErrorHandler.enableSentry("https://2d378b8fef18468ea967f975720ae8ca@sentry.scolasti.co/2", 1.0, v);
          ConsoleLoadingAnimation.disable();
          System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
        } else {
          ConsoleLoadingAnimation.disable();
          System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
          System.out.println(Ansi.ansi().fgYellow().a("[WARNING] ").reset().a("Online error reporting will not be enabled on dev builds!"));
        }
      }
    } catch (Exception e) {
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
