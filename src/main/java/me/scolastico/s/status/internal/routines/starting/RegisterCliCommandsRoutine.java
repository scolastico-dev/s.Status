package me.scolastico.s.status.internal.routines.starting;

import java.util.HashMap;
import me.scolastico.s.status.cli.CopyDefaultFilesCommand;
import me.scolastico.s.status.cli.HelpCommand;
import me.scolastico.s.status.cli.KillCommand;
import me.scolastico.s.status.cli.ReloadConfigsCommand;
import me.scolastico.s.status.cli.SaveConfigCommand;
import me.scolastico.s.status.cli.ShowStatusCommand;
import me.scolastico.s.status.cli.ToggleWebServerLogCommand;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.console.ConsoleManager.ExitCommand;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.web.admin.AdminPanel;
import org.fusesource.jansi.Ansi;

public class RegisterCliCommandsRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      System.out.print(Ansi.ansi().a("Registering cli commands... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      ConsoleManager.setRegisterDefaults(false);
      ConsoleManager.setNotFoundMessage("Command '%' not found! Try 'help' for help.");
      ConsoleManager.registerCommand(new CopyDefaultFilesCommand());
      ConsoleManager.registerCommand(new ExitCommand());
      ConsoleManager.registerCommand(new HelpCommand());
      ConsoleManager.registerCommand(new ShowStatusCommand());
      ConsoleManager.registerCommand(new ReloadConfigsCommand());
      ConsoleManager.registerCommand(new ToggleWebServerLogCommand());
      ConsoleManager.registerCommand(new KillCommand());
      ConsoleManager.registerCommand(new SaveConfigCommand());
      AdminPanel.enableCommands();
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
