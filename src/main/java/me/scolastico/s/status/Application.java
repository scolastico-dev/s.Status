package me.scolastico.s.status;

import com.github.lalyos.jfiglet.FigletFont;
import com.sun.net.httpserver.HttpExchange;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.TxIsolation;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import me.scolastico.mysql.manager.MysqlManager;
import me.scolastico.s.status.cli.CopyDefaultFilesCommand;
import me.scolastico.s.status.cli.HelpCommand;
import me.scolastico.s.status.cli.KillCommand;
import me.scolastico.s.status.cli.ReloadConfigsCommand;
import me.scolastico.s.status.cli.ShowStatusCommand;
import me.scolastico.s.status.cli.ToggleWebServerLogCommand;
import me.scolastico.s.status.database.BaseModel;
import me.scolastico.s.status.database.IncidentArchive;
import me.scolastico.s.status.database.PlannedMaintenance;
import me.scolastico.s.status.database.StatusCheckResult;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.s.status.dataholders.LanguageConfig;
import me.scolastico.s.status.enums.DatabaseType;
import me.scolastico.s.status.internal.etc.CleanUpScheduler;
import me.scolastico.s.status.internal.etc.DefaultScheduler;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.console.ConsoleManager.ExitCommand;
import me.scolastico.tools.dataholder.SchedulerConfiguration;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.simplified.SimplifiedResourceFileReader;
import me.scolastico.tools.web.BrowserFingerprint;
import me.scolastico.tools.web.WebServer;
import me.scolastico.tools.web.interfaces.WebServerPreExecuterInterface;
import org.apache.commons.io.FileUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Application {

  private static ConfigHandler<Config> configHandler;
  private static Config config;
  private static ConfigHandler<LanguageConfig> languageConfigConfigHandler;
  private static LanguageConfig language;
  private static MysqlManager mysqlManager;
  private static Database database;
  private static Thread shutdownHook;
  private static boolean started = false;
  private static boolean showWebServerConnectionLog = true;
  private final static String version = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/VERSION");
  private final static String branch = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/BRANCH");
  private final static String commit = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/COMMIT");

  public static void main(String[] args) {
    PrintStream printStreamBackup = System.out;
    try {
      AnsiConsole.systemInstall();
      ErrorHandler.enableCatchUncaughtException();
      System.out.println(FigletFont.convertOneLine("s.Status"));
      System.out.println("Version: " + version + " | Commit: " + branch + "/" + commit + " | By: scolastico");
      final double startingTime = System.currentTimeMillis();
      System.out.println();

      ConsoleLoadingAnimation.setAnimation(new char[]{'-', '\\', '|', '/'});
      ConsoleLoadingAnimation.setSpeed(25);

      System.out.print(Ansi.ansi().a("Loading config... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      configHandler = new ConfigHandler<>(new Config(), "config.json");
      if (!configHandler.checkIfExists()) {
        configHandler.saveDefaultConfig();
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
        System.out.println("No config found! Generated new one. Please change the default values before restarting.");
        System.out.println(Ansi.ansi().a("Exiting!").reset());
        System.exit(1);
        return;
      }
      config = configHandler.loadConfig();
      if (config.isDebug()) configHandler.storeConfig(config);
      if (config.isEnableErrorLogFile()) ErrorHandler.enableErrorLogFile();
      showWebServerConnectionLog = config.isShowWebServerLog();
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print(Ansi.ansi().a("Loading language... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      languageConfigConfigHandler = new ConfigHandler<>(new LanguageConfig(), "language.json");
      if (!languageConfigConfigHandler.checkIfExists()) {
        languageConfigConfigHandler.saveDefaultConfig();
      }
      language = languageConfigConfigHandler.loadConfig();
      if (config.isDebug()) languageConfigConfigHandler.storeConfig(language);
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      if (config.isReportErrorsOnline()) {
        System.out.print(Ansi.ansi().a("Enabling sentry error reporter... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        String v = version.equals("dev-snapshot") ? commit : version;
        if (!v.equals("IN-DEV")) {
          ErrorHandler.enableSentry("https://2d378b8fef18468ea967f975720ae8ca@sentry.scolasti.co/2", 1.0, v);
          ConsoleLoadingAnimation.disable();
          System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
        } else {
          ConsoleLoadingAnimation.disable();
          System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
          System.out.println(Ansi.ansi().fgYellow().a("[WARNING] ").reset().a("Online error reporting will not be enabled on dev builds!"));
        }
      }

      System.out.print(Ansi.ansi().a("Connecting to database... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      DataSourceConfig dataSourceConfig = new DataSourceConfig();
      MigrationConfig migrationConfig = new MigrationConfig();
      if (config.getDatabaseType() == DatabaseType.SQLITE) {
        migrationConfig.setMigrationPath("dbmigration/sqlite");
        dataSourceConfig.setUrl("jdbc:sqlite:" + config.getSqliteFile());
        dataSourceConfig.setUsername("");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setIsolationLevel(TxIsolation.READ_UNCOMMITTED.getLevel());
      } else {
        if (config.getDatabaseType() == DatabaseType.MYSQL) {
          migrationConfig.setMigrationPath("dbmigration/mysql");
          dataSourceConfig.setUrl("jdbc:mysql://"
              + config.getMysqlHost() + ":"
              + config.getMysqlPort() + "/"
              + config.getMysqlDB()
          );
        } else {
          migrationConfig.setMigrationPath("dbmigration/mariadb");
          dataSourceConfig.setUrl("jdbc:mariadb://"
              + config.getMysqlHost() + ":"
              + config.getMysqlPort() + "/"
              + config.getMysqlDB() + "?useLegacyDatetimeCode=false"
          );
        }
        dataSourceConfig.setUsername(config.getMysqlUser());
        dataSourceConfig.setPassword(config.getMysqlPass());
      }
      DatabaseConfig dbConfig = new DatabaseConfig();
      dbConfig.setDataSourceConfig(dataSourceConfig);
      dbConfig.setName(config.getMysqlDB());
      dbConfig.setDefaultServer(true);
      dbConfig.setClasses(Arrays.asList(BaseModel.class, IncidentArchive.class, PlannedMaintenance.class, StatusCheckResult.class));
      database = DatabaseFactory.create(dbConfig);
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print(Ansi.ansi().a("Running migrations... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      MigrationRunner migrationRunner = new MigrationRunner(migrationConfig);
      migrationRunner.run(database.getDataSource());
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());


      File webFolder = new File("web");
      if (!webFolder.exists() || !webFolder.isDirectory()) {
        System.out.print(Ansi.ansi().a("Copying default files... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        writeDefaultFiles(false);
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
      }

      System.out.print(Ansi.ansi().a("Registering cli commands... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      ConsoleManager.setRegisterDefaults(false);
      ConsoleManager.setNotFoundMessage("Command '%' not found! Try 'help' for help.");
      ConsoleManager.registerCommand(new CopyDefaultFilesCommand());
      ConsoleManager.registerCommand(new ExitCommand());
      ConsoleManager.registerCommand(new HelpCommand());
      ConsoleManager.registerCommand(new ShowStatusCommand());
      ConsoleManager.registerCommand(new ReloadConfigsCommand());
      ConsoleManager.registerCommand(new ToggleWebServerLogCommand());
      ConsoleManager.registerCommand(new KillCommand());
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      if (args.length > 0) {
        System.out.println();
        System.out.println("Detected headless console client usage!");
        System.out.println("Skipping the rest of the startup process now and executing your request!");
        System.out.println();
        System.exit(ConsoleManager.executeCommand(args));
        return;
      }

      System.out.print(Ansi.ansi().a("Starting webserver... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      WebServer.start(config.getPort());
      WebServer.registerAllWebInterfacesInPackage("me.scolastico.s.status.webserver");
      WebServer.setCheckForIndexHtml(true);
      WebServer.setOverrideFolderPath(config.getStaticFolderPath());
      WebServer.setCheckOverrideFolderFirst(true);
      WebServer.setWeightLessPerSecond(1);
      WebServer.setMaxUsageWeight(60);
      WebServer.registerWebServerPreExecuter(
          new WebServerPreExecuterInterface() {
            @Override
            public boolean execute(HttpExchange httpExchange) {
              if (showWebServerConnectionLog && started) {
                BrowserFingerprint fingerprint = new BrowserFingerprint(httpExchange);
                System.out.println(
                    Ansi.ansi().fgYellow().a("[INFO] [WebServer] ").reset().a("CONNECTION FROM "
                            + fingerprint.getIp() + (
                                !fingerprint.getForwardFor().isEmpty() ? "@"
                                    + fingerprint.getForwardFor() : ""
                            ) + " ["
                            + fingerprint.getFingerprintHash() + "] "
                            + httpExchange.getRequestURI().toString()
                    )
                );
              }
              return true;
            }
          });
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print(Ansi.ansi().a("Starting scheduler tasks... ").fgYellow());
      ConsoleLoadingAnimation.enable();
      SchedulerHandler.enable();
      final long defaultSchedulerId = SchedulerHandler.registerTask(new SchedulerConfiguration(20, new Runnable() {
        @Override
        public void run() {
          try {
            DefaultScheduler.run();
          } catch (Exception e) {
            ErrorHandler.handle(e);
          }
        }
      }));
      final long cleanUpSchedulerId = SchedulerHandler.registerTask(new SchedulerConfiguration(config.getRunCleanUpEveryMinutes()*60L*20L, new Runnable() {
        @Override
        public void run() {
          try {
            CleanUpScheduler.run();
          } catch (Exception e) {
            ErrorHandler.handle(e);
          }
        }
      }));
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      if (config.isEnableShutDownHook()) {
        System.out.print(Ansi.ansi().a("Registering shutdown hook... ").fgYellow());
        ConsoleLoadingAnimation.enable();
        shutdownHook = new Thread(new Runnable() {
          @Override
          public void run() {
            printStreamBackup.println();
            printStreamBackup.println();
            printStreamBackup.println("Shutting down...");
            printStreamBackup.println();
            SchedulerHandler.removeConfiguration(defaultSchedulerId);
            SchedulerHandler.removeConfiguration(cleanUpSchedulerId);
            try {
              ConsoleManager.disable();
            } catch (Exception ignored) {}
            AnsiConsole.systemUninstall();
            System.out.println();
            System.out.println("Bye!");
            System.out.println();
          }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        ConsoleLoadingAnimation.disable();
        System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());
      }

      started = true;

      System.out.println();
      System.out.println("Done! Starting took " + ((System.currentTimeMillis()-startingTime)/1000) + " seconds. Try 'help' for help.");
      System.out.println();

      ConsoleManager.enable(true, true);

    } catch (Exception e) {
      System.setOut(printStreamBackup);
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception exception) {
        ErrorHandler.handle(exception);
      }
      System.out.print(Ansi.ansi().reset());
      if (!started) System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
      ErrorHandler.handleFatal(e);
    }
  }

  public static boolean isStarted() {
    return started;
  }

  public static Thread getShutdownHook() {
    return shutdownHook;
  }

  public static boolean isShowWebServerConnectionLog() {
    return showWebServerConnectionLog;
  }

  public static void setShowWebServerConnectionLog(boolean showWebServerConnectionLog) {
    Application.showWebServerConnectionLog = showWebServerConnectionLog;
  }

  public static ConfigHandler<LanguageConfig> getLanguageConfigConfigHandler() {
    return languageConfigConfigHandler;
  }

  public static void setLanguageConfigConfigHandler(ConfigHandler<LanguageConfig> languageConfigConfigHandler) {
    Application.languageConfigConfigHandler = languageConfigConfigHandler;
  }

  public static LanguageConfig getLanguage() {
    return language;
  }

  public static void setLanguage(LanguageConfig language) {
    Application.language = language;
  }

  public static MysqlManager getMysqlManager() {
    return mysqlManager;
  }

  public static String getBranch() {
    return branch;
  }

  public static String getCommit() {
    return commit;
  }

  public static String getVersion() {
    return version;
  }

  public static ConfigHandler<Config> getConfigHandler() {
    return configHandler;
  }

  public static void setConfigHandler(ConfigHandler<Config> configHandler) {
    Application.configHandler = configHandler;
  }

  public static Config getConfig() {
    return config;
  }

  public static void setConfig(Config config) {
    Application.config = config;
  }

  public static void writeDefaultFiles(boolean overwriteFiles) throws IOException {
    SimplifiedResourceFileReader reader = SimplifiedResourceFileReader.getInstance();
    PrintStream tmp = System.out;
    System.setOut(null);
    String[] resourceList = reader.getAllResourcesInFolder("default-web-files");
    System.setOut(tmp);
    for (String path:resourceList) {
      File file = new File("./web/" + path);
      if (!file.exists() || overwriteFiles) {
        FileUtils.writeByteArrayToFile(file, reader.getByteArrayFromResources(path));
      }
    }
  }

}
