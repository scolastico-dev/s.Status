package main;

import java.io.IOException;
import me.scolastico.tools.ebean.DatabaseMigrationGenerator;

class GenerateDBMigration {

  public static void main(String[] args) throws IOException {
    DatabaseMigrationGenerator.create("1.0.0", "initial");
  }

}