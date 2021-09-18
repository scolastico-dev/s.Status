package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import org.fusesource.jansi.Ansi;
import picocli.CommandLine.Command;

@Command(name = "save-config", mixinStandardHelpOptions = true, description = "Save the config to save new fields in it. Useful after an update!", version = "1.0.0")
public class SaveConfigCommand implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    Application.getConfigHandler().storeConfig(Application.getConfig());
    System.out.println(Ansi.ansi().a("Saving config... ").fgGreen().a("[OK]").reset());
    return 0;
  }

}
