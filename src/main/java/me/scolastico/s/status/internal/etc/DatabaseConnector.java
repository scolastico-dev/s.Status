package me.scolastico.s.status.internal.etc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.TxIsolation;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import me.scolastico.s.status.dataholders.DatabaseConfig;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {

  private static Database database = null;
  private static final DataSourceConfig dataSourceConfig = new DataSourceConfig();
  private static final MigrationConfig migrationConfig = new MigrationConfig();
  private static final io.ebean.config.DatabaseConfig dbConfig = new io.ebean.config.DatabaseConfig();
  private static DatabaseConfig config = null;
  private static boolean started = false;
  private static boolean debug = false;
  private static boolean migrated = false;

  public static synchronized void connectToDatabase(DatabaseConfig config) {
    if (!started) {
      LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
      Logger ebeanLogger = loggerContext.getLogger("io.ebean");
      Logger reflectionsLogger = loggerContext.getLogger("org.reflections");
      debug = config.isDebug();
      if (debug) {
        ebeanLogger.setLevel(Level.DEBUG);
        reflectionsLogger.setLevel(Level.DEBUG);
      } else {
        ebeanLogger.setLevel(Level.WARN);
        reflectionsLogger.setLevel(Level.WARN);
      }
      DatabaseConnector.config = config;
      dataSourceConfig.setUsername(config.getUsername());
      dataSourceConfig.setPassword(config.getPassword());
      migrationConfig.setMigrationPath("dbmigration/" + config.getDatabaseType().name);
      switch (config.getDatabaseType().name) {
        case "sql":
          dataSourceConfig.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          dataSourceConfig.setUrl("jdbc:sqlserver://"
              + config.getHost() + ":"
              + config.getPort() + ";"
              + "databaseName=" + config.getDatabase()
          );
          break;

        case "mysql":
          dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
          dataSourceConfig.setUrl("jdbc:mysql://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase()
          );
          break;

        case "mariadb":
          dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
          dataSourceConfig.setUrl("jdbc:mariadb://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase() + "?useLegacyDatetimeCode=false"
          );
          break;

        case "sqlite":
          dataSourceConfig.setDriver("org.sqlite.JDBC");
          dataSourceConfig.setUrl("jdbc:sqlite:" + config.getHost());
          dataSourceConfig.setIsolationLevel(TxIsolation.READ_UNCOMMITTED.getLevel());
          break;

        case "postgres":
          dataSourceConfig.setDriver("org.postgresql.Driver");
          dataSourceConfig.setUrl("jdbc:postgresql://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase()
          );
          break;

        case "oracle":
          dataSourceConfig.setDriver("oracle.jdbc.driver.OracleDriver");
          dataSourceConfig.setUsername(config.getDatabase());
          dataSourceConfig.setUrl("jdbc:oracle:thin:@"
              + config.getHost() + ":"
              + config.getPort() + ":XE");
          break;

        case "h2":
          dataSourceConfig.setDriver("org.h2.Driver");
          dataSourceConfig.setUrl("jdbc:h2:mem" + config.getDatabase());
          break;
      }
      dbConfig.setDataSourceConfig(dataSourceConfig);
      dbConfig.setName(config.getDatabase());
      dbConfig.setDefaultServer(true);
      database = DatabaseFactory.create(dbConfig);
      started = true;
    }
  }

  public static synchronized void runMigrations() {
    if (!migrated) {
      MigrationRunner migrationRunner = new MigrationRunner(migrationConfig);
      migrationRunner.run(database.getDataSource());
      migrated = true;
    }
  }

  public static Database getDatabase() {
    return database;
  }

  public static DataSourceConfig getDataSourceConfig() {
    return dataSourceConfig;
  }

  public static MigrationConfig getMigrationConfig() {
    return migrationConfig;
  }

  public static io.ebean.config.DatabaseConfig getDbConfig() {
    return dbConfig;
  }

  public static DatabaseConfig getConfig() {
    return config;
  }

  public static boolean isStarted() {
    return started;
  }

  public static boolean isDebug() {
    return debug;
  }

  public static boolean isMigrated() {
    return migrated;
  }

}
