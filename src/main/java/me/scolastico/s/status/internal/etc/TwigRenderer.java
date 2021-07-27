package me.scolastico.s.status.internal.etc;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.sun.net.httpserver.HttpExchange;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import me.scolastico.s.status.Application;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.web.CookieManager;

public class TwigRenderer {

  private static final PebbleEngine pebbleEngine = new PebbleEngine.Builder().loader(new FileLoader()).build();

  public static String renderTwigTemplate(HttpExchange exchange, String fileName, Map<String, Object> twigValues) {
    try {
      String language = CookieManager.getCookie(exchange, "language");
      twigValues.put("language", Application.getLanguage().getLanguage(language));
      twigValues.put("twigFolder", Application.getConfig().getTwigFolderPath());
      PebbleTemplate template = pebbleEngine.getTemplate(Application.getConfig().getTwigFolderPath() + fileName);
      Writer writer = new StringWriter();
      template.evaluate(writer, twigValues);
      return writer.toString();
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return null;
  }

}
