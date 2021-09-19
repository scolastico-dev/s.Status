package me.scolastico.s.status.internal.routines.starting;

import java.io.PrintStream;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class RegisterShutdownHookRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      if (Application.getConfig().isEnableShutDownHook()) {
        System.out.print(Ansi.ansi().a("Registering shutdown hook... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        Thread shutdownHook = new Thread(new Runnable() {
          @Override
          public void run() {
            PrintStream printStreamBackup = Application.getPrintStreamBackup();
            printStreamBackup.println();
            printStreamBackup.println();
            printStreamBackup.println("Shutting down...");
            printStreamBackup.println();
            SchedulerHandler.removeConfiguration((Long) objectMap.get("defaultSchedulerId"));
            SchedulerHandler.removeConfiguration((Long) objectMap.get("cleanUpSchedulerId"));
            try {
              ConsoleManager.disable();
            } catch (Exception ignored) {}
            AnsiConsole.systemUninstall();
            System.out.println();
            System.out.println("Bye!");
            System.out.println();
          }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        objectMap.put("shutdownHook", shutdownHook);
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
