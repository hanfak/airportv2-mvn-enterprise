package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.infrastructure.httpclient.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static com.hanfak.airport.domain.monitoring.ProbeStatus.FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherApiHealthCheckTest {

  @Test
  public void nameMentionsWeatherApiURL() {
    WeatherApiHealthCheck weatherApiHealthCheck = new WeatherApiHealthCheck(settings, client);

    assertThat(weatherApiHealthCheck.name()).isEqualTo("Weather Api Connection to 'blah'");
  }

  @Test
  public void returnsFailureWhenExceptionOccursInCallToWeatherApiURL() throws Exception {
    UnirestException cause = new UnirestException("Blah Error");
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(client.submitGetRequest(any(), any()))
            .thenThrow(cause);

    WeatherApiHealthCheck weatherApiHealthCheck = new WeatherApiHealthCheck(settings, client);
    ProbeResult result = weatherApiHealthCheck.probe();

    assertThat(result.description).isEqualTo("Call to Weather Api threw an exception, Blah Error");
    assertThat(result.status).isEqualTo(FAIL);
  }

  @Test
  public void returnsFailureWhenNonSuccessfulResponseFromWeatherApi() throws Exception {
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(response.getStatus()).thenReturn(404);

    when(client.submitGetRequest(any(), any()))
            .thenReturn(response);

    WeatherApiHealthCheck weatherApiHealthCheck = new WeatherApiHealthCheck(settings, client);
    ProbeResult result = weatherApiHealthCheck.probe();

    assertThat(result.description).isEqualTo("Call to Weather Api returned unexpected status code '404'");
    assertThat(result.status).isEqualTo(FAIL);
  }

  private Settings settings() {
    Settings settings = mock(Settings.class);
    when(settings.weatherUrl()).thenReturn(WEATHER_API_URL);
    return settings;
  }

  private static final String WEATHER_API_URL = "blah";

  private final Settings settings = settings();
  private final UnirestHttpClient client = mock(UnirestHttpClient.class);
  private final HttpResponse response = mock(HttpResponse.class);

}
