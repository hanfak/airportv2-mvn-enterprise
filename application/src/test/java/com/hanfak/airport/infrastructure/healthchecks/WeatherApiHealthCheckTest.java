package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.infrastructure.dataproviders.weather.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherApiHealthCheckTest {

  @Test
  public void nameMentionsWeatherApiURL() {
    WeatherApiHealthCheck weatherApiHealthCheck = new WeatherApiHealthCheck(settings, client);

    assertThat(weatherApiHealthCheck.name()).isEqualTo("Weather Api Connection to 'blah'");
  }

  // TODO test for IO/unirest exception = failing

  // TODO test for unexpected status code = failing

  private Settings settings() {
    Settings settings = mock(Settings.class);
    when(settings.databaseUrl()).thenReturn(WEATHER_API_URL);
    return settings;
  }

  private static final String WEATHER_API_URL = "blah";

  private final Settings settings = settings();
  private final UnirestHttpClient client = mock(UnirestHttpClient.class);
}
