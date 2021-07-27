package me.scolastico.s.status.dataholders;

import java.util.HashMap;

public class LanguageConfig {

  private String defaultLanguage = "en";
  private HashMap<String, Language> languages = new HashMap<>(){{
    put("en", new Language());
  }};

  public Language getLanguage(String id) {
    return languages.getOrDefault(id, languages.getOrDefault(defaultLanguage, new Language()));
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public void setDefaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  public HashMap<String, Language> getLanguages() {
    return languages;
  }

  public void setLanguages(HashMap<String, Language> languages) {
    this.languages = languages;
  }

}
