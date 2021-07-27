package me.scolastico.s.status.cli;

import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import org.fusesource.jansi.Ansi;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "copy-default-files", description = "Copy the default files in the web folder.", mixinStandardHelpOptions = true, version = "1.0.0")
public class CopyDefaultFilesCommand implements Callable<Integer> {

  @Option(names = {"-o", "--overwrite"}, description = "Overwrite already existing files.")
  private boolean override;

  @Override
  public Integer call() throws Exception {
    Application.writeDefaultFiles(override);
    System.out.println(Ansi.ansi().fgGreen().a(override ? "Overwritten default files!" : "Copied default files!").reset());
    return null;
  }

}
