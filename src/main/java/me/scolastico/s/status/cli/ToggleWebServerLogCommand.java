package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import picocli.CommandLine.Command;

@Command(name = "toggle-webserver-log", mixinStandardHelpOptions = true, description = "Toggle the webserver log. Useful to prevent spam while interacting with the console.", version = "1.0.0")
public class ToggleWebServerLogCommand implements Callable<Integer> {
  @Override
  public Integer call() throws Exception {
    Application.setShowWebServerConnectionLog(!Application.isShowWebServerConnectionLog());
    if (Application.isShowWebServerConnectionLog()) {
      System.out.println("Enabled webserver log!");
    } else {
      System.out.println("Disabled webserver log!");
    }
    return 0;
  }
}
