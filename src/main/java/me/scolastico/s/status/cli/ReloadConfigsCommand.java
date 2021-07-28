package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import me.scolastico.tools.handler.ErrorHandler;
import org.fusesource.jansi.Ansi;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "reload", mixinStandardHelpOptions = true, version = "1.0.0", description = "Reload config files.")
public class ReloadConfigsCommand implements Callable<Integer> {

  @Option(names = {"-l", "--language"}, description = "Reload only the language file.")
  private boolean onlyLanguage;

  @Option(names = {"-c", "--config"}, description = "Reload only the config.")
  private boolean onlyConfig;

  @Override
  public Integer call() throws Exception {
    if (onlyLanguage && onlyConfig) {
      System.out.println(Ansi.ansi().fgRed().a("[ERROR] Dont combine '-c' and '-l'.").reset());
      System.out.println(Ansi.ansi().fgRed().a("[ERROR] If you want to reload all configs execute the command without args.").reset());
      return 0;
    } else if (onlyLanguage) {
      reloadLanguage();
    } else if (onlyConfig) {
      reloadConfig();
    } else {
      reloadLanguage();
      reloadConfig();
    }
    System.out.println(Ansi.ansi().fgGreen().a("Reloaded config!").reset());
    System.out.println(Ansi.ansi().fgYellow().a("[WARNING] Some configs can only be used if you restart the application.").reset());
    System.out.println(Ansi.ansi().fgYellow().a("[WARNING] Example: port changes").reset());
    return 0;
  }

  private void reloadConfig() {
    try {
      Application.setConfig(Application.getConfigHandler().loadConfig());
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }

  private void reloadLanguage() {
    try {
      Application.setLanguage(Application.getLanguageConfigConfigHandler().loadConfig());
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }

}
