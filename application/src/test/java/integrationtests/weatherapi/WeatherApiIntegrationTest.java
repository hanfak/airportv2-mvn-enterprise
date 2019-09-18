package integrationtests.weatherapi;

import com.hanfak.airport.infrastructure.dataproviders.weather.WeatherClient;
import com.hanfak.airport.infrastructure.httpclient.*;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import org.junit.Ignore;
import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.slf4j.LoggerFactory.getLogger;

public class WeatherApiIntegrationTest {

  @Test
  @Ignore // Only run when needed, as talking to real api
  public void callsWeatherApi() {
    Path appProperties = Paths.get("target/classes/localhost.application.properties");
    Path secretsProperties = Paths.get("unused");
    Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
    TestLogger testLogger = new TestLogger();
    HttpClient httpClient = new LoggingHttpClient(testLogger, new UnirestHttpClient(settings), new TimerFactory(), new LogObfuscator());
    WeatherClient weatherClient = new WeatherClient(httpClient, settings, testLogger);
//    int weatherId = weatherClient.getWeatherId();
//
//    assertThat(weatherId).isGreaterThan(0);

    System.out.println(weatherClient.getWeatherId());
  }
}
