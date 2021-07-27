package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import picocli.CommandLine.Command;

@Command(name = "kill", aliases = {"kys"}, mixinStandardHelpOptions = true, description = "Kill the software without soft shutdown functions.", version = "1.0.0")
public class KillCommand implements Callable<Integer> {
  @Override
  public Integer call() throws Exception {
    if (Application.getShutdownHook() != null) Runtime.getRuntime().removeShutdownHook(Application.getShutdownHook());
    System.exit(0);
    return 0;
  }
}
