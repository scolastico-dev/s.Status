package me.scolastico.s.status.internal.routines.starting;

import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.internal.etc.DefaultScheduler;
import me.scolastico.s.status.internal.routines.cleanup.DeleteOldArchiveRoutine;
import me.scolastico.s.status.internal.routines.cleanup.DeleteOldResultsRoutine;
import me.scolastico.s.status.internal.routines.cleanup.UpdateResultTimeRoutine;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.dataholder.SchedulerConfiguration;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.routine.RoutineManager;
import org.fusesource.jansi.Ansi;

public class StartSchedulerTasksRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Starting scheduler tasks... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      SchedulerHandler.enable();
      final ArrayList<Routine> routines = new ArrayList<>();
      routines.add(new DeleteOldArchiveRoutine());
      routines.add(new DeleteOldResultsRoutine());
      routines.add(new UpdateResultTimeRoutine());
      final long defaultSchedulerId = SchedulerHandler.registerTask(new SchedulerConfiguration(20, new Runnable() {
        @Override
        public void run() {
          try {
            DefaultScheduler.run();
          } catch (Exception e) {
            ErrorHandler.handle(e);
          }
        }
      }));
      final long cleanUpSchedulerId = SchedulerHandler.registerTask(new SchedulerConfiguration(Application.getConfig().getRunCleanUpEveryMinutes()*60L*20L, new Runnable() {
        @Override
        public void run() {
          try {
            new RoutineManager(routines).start();
          } catch (Exception e) {
            ErrorHandler.handle(e);
          }
        }
      }));
      objectMap.put("defaultSchedulerId", defaultSchedulerId);
      objectMap.put("cleanUpSchedulerId", cleanUpSchedulerId);
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
