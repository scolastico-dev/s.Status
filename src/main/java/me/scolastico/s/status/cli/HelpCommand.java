package me.scolastico.s.status.cli;

import com.github.freva.asciitable.AsciiTable;
import java.util.concurrent.Callable;
import me.scolastico.s.status.Application;
import picocli.CommandLine.Command;

@Command(name = "help", description = "Shows a help page with a list of all commands and a short description.")
public class HelpCommand implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    String[] headers = {"Command", "Description"};
    String[][] data = {
        {"help", "Shows this help page."},
        {"status", "Shows the status for the checks."},
        {"reload", "Reload the configuration files."},
        {"toggle-webserver-log", "Toggle the webserver log."},
        {"copy-default-files", "Copy the default files for the 'web' folder."},
        {"ap-add-user", "Add an user for the admin panel."},
        {"ap-remove-user", "Remove a user from the admin panel."},
        {"ap-passwd", "Change the password of an admin panel user."},
        {"ap-change-permissions", "Change the permissions of an admin panel user."},
        {"ap-show-permissions", "Show which permissions a admin panel user has."},
        {"ap-show-users", "Show a list of all users in the admin panel."},
        {"kill", "Exit s.Status without the soft-shutdown functions instantly."},
        {"exit", "Exit s.Status and return to your command line."}
    };
    String[] generatedTable = AsciiTable.getTable(headers, data).split("\\r?\\n|\\r");
    System.out.println();
    System.out.println("s.Status - Version " + Application.getVersion() + " - " + Application.getBranch() + "/" + Application.getCommit());
    for(String output:generatedTable) {
      System.out.println(output);
    }
    System.out.println("Use '--help' after an command to show command specific help pages.");
    System.out.println();
    return 0;
  }

}
