package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.dataholders.LanguageConfig;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class LoadLanguageRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Loading language... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      ConfigHandler<LanguageConfig> languageConfigConfigHandler = new ConfigHandler<>(new LanguageConfig(), "language.json");
      if (!languageConfigConfigHandler.checkIfExists()) {
        languageConfigConfigHandler.saveDefaultConfig();
      }
      LanguageConfig language = languageConfigConfigHandler.loadConfig();
      if (Application.getConfig().isDebug()) languageConfigConfigHandler.storeConfig(language);
      Application.setLanguage(language);
      Application.setLanguageConfigConfigHandler(languageConfigConfigHandler);
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
