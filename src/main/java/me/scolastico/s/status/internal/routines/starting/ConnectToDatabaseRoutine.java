package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.ebean.DatabaseConnector;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class ConnectToDatabaseRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      DatabaseConnector connector = new DatabaseConnector(true);

      System.out.print("Loading database drivers... ");
      ConsoleLoadingAnimation.enable();
      DatabaseConnector.loadDatabaseDrivers();
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print(Ansi.ansi().a("Connecting to database... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      connector.connectToDatabase(Application.getConfig().getDatabaseConfig());
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print(Ansi.ansi().a("Running migrations... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      connector.runMigrations();
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      return new RoutineAnswer(objectMap);
    } catch (Exception e) {
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
