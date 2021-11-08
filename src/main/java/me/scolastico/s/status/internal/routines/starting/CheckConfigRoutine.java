package me.scolastico.s.status.internal.routines.starting;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.s.status.dataholders.StatusCheck;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class CheckConfigRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Checking config... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      Config config = Application.getConfig();
      boolean changed = false;
      ArrayList<StatusCheck> checks = new ArrayList<>();
      for (StatusCheck check:config.getStatusChecks()) {
        String cleanId = check.getId().replaceAll("[^a-zA-Z0-9]", "");
        if (!check.getId().equals(cleanId)) {
          check.setId(cleanId);
          changed = true;
        }
        checks.add(check);
      }
      if (changed) {
        config.setStatusChecks(checks.toArray(new StatusCheck[0]));
        Application.getConfigHandler().storeConfig(config);
        Application.setConfig(config);
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
        System.out.println("[WARNING] Config uses unsupported chars in ids. Removed all unsupported chars in the ids.");
      } else {
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
      }
      return new RoutineAnswer(objectMap);
    } catch (Exception e) {
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
