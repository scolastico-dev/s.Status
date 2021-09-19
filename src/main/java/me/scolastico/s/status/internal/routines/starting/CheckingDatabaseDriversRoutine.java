package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class CheckingDatabaseDriversRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Checking database drivers... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      Class.forName("com.mysql.cj.jdbc.Driver");
      Class.forName("org.mariadb.jdbc.Driver");
      Class.forName("org.sqlite.JDBC");
      Class.forName("org.postgresql.Driver");
      Class.forName("oracle.jdbc.driver.OracleDriver");
      Class.forName("org.h2.Driver");
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
    } catch (Exception e) {
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ConsoleLoadingAnimation.disable();
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
