package me.scolastico.s.status.dataholders;

public class PageInfoCardDataHolder {

  private final String warning;
  private final String error;

  public PageInfoCardDataHolder(String message, boolean isError) {
    if (isError) {
      this.error = message;
      this.warning = null;
    } else {
      this.warning = message;
      this.error = null;
    }
  }

  public String getWarning() {
    return warning;
  }

  public String getError() {
    return error;
  }

}
