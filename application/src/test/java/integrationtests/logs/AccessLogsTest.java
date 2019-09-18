package integrationtests.logs;

import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import com.hanfak.airport.wiring.configuration.Wiring;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Test;
import testinfrastructure.ConsoleOutputCapturer;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class AccessLogsTest {

  @Test
  public void returnsStandardMessageForUncaughtErrors() throws UnirestException {
    ConsoleOutputCapturer consoleOutputCapturer = new ConsoleOutputCapturer();
    consoleOutputCapturer.start();
    application.startWebserver();

    whenAnEndpointIsRequestedThatCausesAnError();

    String logs = consoleOutputCapturer.stop();
    assertThat(logs).contains("[LOG_TYPE=ACCESS] 127.0.0.1");
  }

  private void whenAnEndpointIsRequestedThatCausesAnError() throws UnirestException {
    Unirest.get(apiUrl).asString();
  }

  @After
  public void teardown() {
    application.stopWebServer();
  }

  private final String apiPath = "/metrics";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555" + apiPath;
  private final Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
  private final Path secretsProperties = Paths.get("unused");
  private final Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
  private final Wiring wiring = Wiring.wiring(settings);
  private final Application application = new Application(wiring);
}
