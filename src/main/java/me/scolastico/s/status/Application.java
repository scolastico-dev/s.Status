package me.scolastico.s.status;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.s.status.dataholders.LanguageConfig;
import me.scolastico.s.status.internal.routines.starting.CheckConfigRoutine;
import me.scolastico.s.status.internal.routines.starting.ConnectToDatabaseRoutine;
import me.scolastico.s.status.internal.routines.starting.CopyDefaultFilesRoutine;
import me.scolastico.s.status.internal.routines.starting.EnableSentryRoutine;
import me.scolastico.s.status.internal.routines.starting.FooterRoutine;
import me.scolastico.s.status.internal.routines.starting.HeaderRoutine;
import me.scolastico.s.status.internal.routines.starting.LoadConfigRoutine;
import me.scolastico.s.status.internal.routines.starting.LoadLanguageRoutine;
import me.scolastico.s.status.internal.routines.starting.RegisterCliCommandsRoutine;
import me.scolastico.s.status.internal.routines.starting.RegisterShutdownHookRoutine;
import me.scolastico.s.status.internal.routines.starting.RunHeadlessModeRoutine;
import me.scolastico.s.status.internal.routines.starting.SettingLoggingRoutine;
import me.scolastico.s.status.internal.routines.starting.StartSchedulerTasksRoutine;
import me.scolastico.s.status.internal.routines.starting.StartWebServerRoutine;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineManager;
import me.scolastico.tools.simplified.SimplifiedResourceFileReader;
import org.apache.commons.io.FileUtils;

public class Application {

  private static ConfigHandler<Config> configHandler;
  private static Config config;
  private static ConfigHandler<LanguageConfig> languageConfigConfigHandler;
  private static LanguageConfig language;
  private static Thread shutdownHook;
  private static boolean started = false;
  private static boolean showWebServerConnectionLog = true;
  private static PrintStream printStreamBackup;
  private final static String version = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/VERSION");
  private final static String branch = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/BRANCH");
  private final static String commit = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/COMMIT");

  public static void main(String[] args) {
    printStreamBackup = System.out;
    try {
      ArrayList<Routine> routines = new ArrayList<>();
      routines.add(new HeaderRoutine());
      routines.add(new LoadConfigRoutine());
      routines.add(new CheckConfigRoutine());
      routines.add(new LoadLanguageRoutine());
      routines.add(new SettingLoggingRoutine());
      routines.add(new EnableSentryRoutine());
      routines.add(new ConnectToDatabaseRoutine());
      routines.add(new CopyDefaultFilesRoutine());
      routines.add(new RegisterCliCommandsRoutine());
      routines.add(new RunHeadlessModeRoutine());
      routines.add(new StartWebServerRoutine());
      routines.add(new StartSchedulerTasksRoutine());
      routines.add(new RegisterShutdownHookRoutine());
      routines.add(new FooterRoutine());
      RoutineManager manager = new RoutineManager(routines);
      HashMap<String, Object> objectMap = new HashMap<>();
      objectMap.put("args", args);
      manager.startNotAsynchronously(objectMap);

      if (manager.isCanceled()) {
        System.exit(1);
        return;
      }

      shutdownHook = (Thread) manager.getLastObjectMap().get("shutdownHook");

      started = true;
    } catch (Exception e) {
      ErrorHandler.handleFatal(e);
    }
  }

  public static PrintStream getPrintStreamBackup() {
    return printStreamBackup;
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
