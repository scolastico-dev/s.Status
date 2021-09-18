package main;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;

class GenerateDBMigration {

  /**
   * Generate the DDL for the next DB migration.
   */
  public static void main(String[] args) throws IOException {
    DbMigration dbMigration = DbMigration.create();
    dbMigration.setVersion("1.0.0");
    dbMigration.setName("initial");
    dbMigration.addPlatform(Platform.SQLSERVER17, "sql");
    dbMigration.addPlatform(Platform.MYSQL, "mysql");
    dbMigration.addPlatform(Platform.MARIADB, "mariadb");
    dbMigration.addPlatform(Platform.SQLITE, "sqlite");
    dbMigration.addPlatform(Platform.POSTGRES, "postgres");
    dbMigration.addPlatform(Platform.ORACLE, "oracle");
    dbMigration.addPlatform(Platform.H2, "h2");
    dbMigration.generateMigration();
  }

}