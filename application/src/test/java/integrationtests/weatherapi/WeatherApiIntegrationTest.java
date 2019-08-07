package integrationtests.weatherapi;

import com.hanfak.airport.infrastructure.dataproviders.weather.UnirestHttpClient;
import com.hanfak.airport.infrastructure.dataproviders.weather.WeatherClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import org.junit.Ignore;
import org.junit.Test;
import testinfrastructure.TestLogger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class WeatherApiIntegrationTest {

  @Test
  @Ignore // Only run when needed, as talking to real api
  public void callsWeatherApi() {
    Path appProperties = Paths.get("target/classes/localhost.application.properties");
    Path secretsProperties = Paths.get("unused");
    Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
    TestLogger testLogger = new TestLogger();
    UnirestHttpClient unirestHttpClient = new UnirestHttpClient();
    WeatherClient weatherClient = new WeatherClient(unirestHttpClient, settings, testLogger);
    int weatherId = weatherClient.getWeatherId();

    assertThat(weatherId).isGreaterThan(0);
  }
}