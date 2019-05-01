package com.hanfak.airport.wiring;

import com.google.common.annotations.VisibleForTesting;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.infrastructure.webserver.JettyWebServer;
import com.hanfak.airport.wiring.configuration.Wiring;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static com.hanfak.airport.infrastructure.webserver.EndPoint.get;
import static com.hanfak.airport.infrastructure.webserver.EndPoint.post;
import static com.hanfak.airport.wiring.configuration.Wiring.wiring;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"PMD.TooManyStaticImports", "PMD.SystemPrintln"}) // These all refer to domain objects
public class Application {

  private final Wiring wiring;
  private JettyWebServer webserver;

  public Application(Wiring wiring) {
    this.wiring = wiring;
  }
// Example of running application, need to remove when better interface available
  public static void main(String... args) {
    //TODO: get properties from path that docker can access
    Path appProperties = Paths.get("application/target/classes/localhost.application.properties");
    Path secretsProperties = Paths.get("unused");
    Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
    Application application = new Application(wiring(settings));
    System.out.println("Airport application started...");
    application.startWebserver();
  }

  public void startWebserver() {
    webserver = wiring.jettyWebServerBuilder()
            // todo test paths
            .registerReadyPageEndPoint(get("/ready"), wiring.readyPageServlet())
            .registerEndPoint(post("/landAirplane"), wiring.landAirplaneServlet())
            .build();
    webserver.startServer();
  }

  @VisibleForTesting
  public void stopWebServer() {
    webserver.stopServer();
  }
}
