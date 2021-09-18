package me.scolastico.s.status.enums;

public enum DataBaseType {
  SQL("sql"),
  MYSQL("mysql"),
  MARIADB("mariadb"),
  SQLITE("sqlite"),
  POSTGRES("postgres"),
  ORACLE("oracle"),
  H2("h2");

  public final String name;

  private DataBaseType(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
