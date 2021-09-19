package me.scolastico.s.status.internal.routines.starting;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import me.scolastico.s.status.Application;
import me.scolastico.s.status.dataholders.Config;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.web.BrowserFingerprint;
import me.scolastico.tools.web.WebServer;
import me.scolastico.tools.web.interfaces.WebServerPreExecuterInterface;
import org.fusesource.jansi.Ansi;

public class StartWebServerRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    try {
      Config config = Application.getConfig();
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
              if (config.isShowWebServerLog() && Application.isStarted()) {
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
    } catch (Exception e) {
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]"));
      ConsoleLoadingAnimation.disable();
      ErrorHandler.handleFatal(e);
      return new RoutineAnswer(true, "exception");
    }
    return new RoutineAnswer(objectMap);
  }

}
