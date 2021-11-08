package me.scolastico.s.status.internal.routines.starting;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;
import org.slf4j.LoggerFactory;

public class SettingLoggingRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Setting logging behaviour... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
      Logger ebeanLogger = loggerContext.getLogger("io.ebean");
      Logger reflectionsLogger = loggerContext.getLogger("org.reflections");
      if (!Application.getConfig().isDebug()) {
        ebeanLogger.setLevel(Level.WARN);
        reflectionsLogger.setLevel(Level.WARN);
      } else {
        ebeanLogger.setLevel(Level.DEBUG);
        reflectionsLogger.setLevel(Level.DEBUG);
      }
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
    } catch (Exception e) {
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
