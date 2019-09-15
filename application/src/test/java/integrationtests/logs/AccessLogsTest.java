package integrationtests.logs;

import com.hanfak.airport.infrastructure.webserver.JettyWebServer;
import com.hanfak.airport.infrastructure.webserver.RequestLogFactory;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import testinfrastructure.ConsoleOutputCapturer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
// TODO Flakey, use app endpoint instead
// TODO write in BDD format
public class AccessLogsTest {

  @Test
  public void returnsStandardMessageForUncaughtErrors() throws UnirestException {
    ConsoleOutputCapturer consoleOutputCapturer = new ConsoleOutputCapturer();
    consoleOutputCapturer.start();
    startWebServer();

    whenAnEndpointIsRequestedThatCausesAnError();

    String logs = consoleOutputCapturer.stop();
    assertThat(logs).contains("[LOG_TYPE=ACCESS] 127.0.0.1");
  }

  private void whenAnEndpointIsRequestedThatCausesAnError() throws UnirestException {
    Unirest.get(apiUrl).asString();
  }

  private void startWebServer() {
    servletContextHandler.addServlet(new ServletHolder(someServlet), "/someendpoint");
    jettyWebServer
            .withContext(servletContextHandler)
            .withRequestLog(RequestLogFactory.createRequestLog());
    jettyWebServer.startServer();
  }

  @After
  public void teardown() {
    jettyWebServer.stopServer();
  }

  private final String apiPath = "/someendpoint";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5558" + apiPath;

  private final ServletContextHandler servletContextHandler = new ServletContextHandler();
  private final SomeServlet someServlet = new SomeServlet();
  private final Logger logger = mock(Logger.class);
  private final JettyWebServer jettyWebServer = new JettyWebServer(5558, logger);

  private class SomeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
      response.getWriter().write("Hello");
    }
  }
}
