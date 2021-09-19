package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class LoadConfigRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Loading config... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      ConfigHandler<Config> configHandler = new ConfigHandler<>(new Config(), "config.json");
      if (!configHandler.checkIfExists()) {
        configHandler.saveDefaultConfig();
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
        System.out.println();
        System.out.println("No config found! Generated new one. Please change the default values before restarting.");
        System.out.println(Ansi.ansi().a("Exiting!").reset());
        System.exit(1);
        return new RoutineAnswer(true, "no config");
      }
      Config config = configHandler.loadConfig();
      if (config.isDebug()) configHandler.storeConfig(config);
      if (config.isEnableErrorLogFile()) ErrorHandler.enableErrorLogFile();
      Application.setConfig(config);
      Application.setConfigHandler(configHandler);
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
