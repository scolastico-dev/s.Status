package me.scolastico.s.status.dataholders;

import me.scolastico.s.status.enums.DataBaseType;

public class DatabaseConfig {

  private DataBaseType databaseType = DataBaseType.SQLITE;
  private String host = "database.sqlite";
  private int port = 3306;
  private String database = "database";
  private String username = "username";
  private String password = "P4$$word";
  private boolean debug = false;

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public DataBaseType getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(DataBaseType databaseType) {
    this.databaseType = databaseType;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
