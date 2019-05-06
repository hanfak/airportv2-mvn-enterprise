package integrationtests.rest.uncaughterrorpage;

import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.infrastructure.webserver.JettyWebServer;
import com.hanfak.airport.infrastructure.webserver.UncaughtErrorHandler;
import com.hanfak.airport.wiring.Application;
import com.hanfak.airport.wiring.configuration.Wiring;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

//https://www.journaldev.com/1973/servlet-exception-and-error-handling-example-tutorial
public class UncaughtErrorPageTest {
  // Do this as a security prevention. To avoid displaying classes and sensitive details in the response
  @Test
  public void returnsStandardMessageForUncaughtErrors() throws UnirestException {
    startWebServer();

    whenAnEndpointIsRequestedThatCausesAnError();

    assertThat(responseBody).isEqualTo("Technical Failure. Please contact the system administrator.");
    assertThat(responseStatus).isEqualTo(500);
  }

  private void whenAnEndpointIsRequestedThatCausesAnError() throws UnirestException {
    HttpResponse<String> httpResponse = Unirest.get(apiUrl)
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
  }

  public void startWebServer() {
    servletContextHandler.addServlet(new ServletHolder(errorServlet), "/errorendpoint");
    jettyWebServer.withBean(new UncaughtErrorHandler(getLogger(APPLICATION.name())))
            .withContext(servletContextHandler);
    jettyWebServer.startServer();
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/errorendpoint";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555" + apiPath;

  private final Path appProperties = Paths.get("target/classes/localhost.application.properties");
  private final Path secretsProperties = Paths.get("unused");
  private final Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);

  private final Wiring wiring = Wiring.wiring(settings);
  private final ServletContextHandler servletContextHandler = new ServletContextHandler();
  private final ErrorServlet errorServlet = new ErrorServlet();
  private final JettyWebServer jettyWebServer = wiring.jettyWebServer(5555);

  private class ErrorServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
      throw new ServletException("GET method is not supported.");
    }
  }
}
