package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "status", mixinStandardHelpOptions = true, description = "Show the current status for the checks.", version = "1.0.0")
public class ShowStatusCommand implements Callable<Integer> {

  @Option(names = {"-d", "--details"}, description = "Check ID")
  private String id;

  @Override
  public Integer call() throws Exception {

    return 0;
  }

}
