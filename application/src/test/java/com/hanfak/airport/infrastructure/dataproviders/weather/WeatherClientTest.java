package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.infrastructure.httpclient.HttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherClientTest {

  @Test
  public void throwExceptionWithNetworkProblem() throws Exception {
    UnirestException cause = new UnirestException("blah");
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(httpClient.submitGetRequest(any(), any())).thenThrow(cause);

    assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected exception when getting weather from api")
            .hasCause(cause);
    assertThat(logger.errorCauses()).contains(cause);
    assertThat(logger.errorLogs()).contains("Unexpected exception when getting weather from api");
  }

  @Test
  public void throwExceptionWhenLoggingRequest() throws Exception {
    IOException cause = new IOException("blah");
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(httpClient.submitGetRequest(any(), any())).thenThrow(cause);

    assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected exception when getting weather from api")
            .hasCause(cause);
    assertThat(logger.errorCauses()).contains(cause);
    assertThat(logger.errorLogs()).contains("Unexpected exception when getting weather from api");
  }

  @Test
  public void throwExceptionWitInvalidJson() throws Exception {
    JSONException cause = new JSONException("blah");
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(httpClient.submitGetRequest(any(), any())).thenThrow(cause);

    assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected exception when getting weather from api")
            .hasCause(cause);
    assertThat(logger.errorCauses()).contains(cause);
    assertThat(logger.errorLogs()).contains("Unexpected exception when getting weather from api");
  }

  @Test
  public void throwExceptionWithWrongStatusCode() throws UnirestException, IOException {
    when(settings.appId()).thenReturn("blahSettings");
    when(settings.locationLongitude()).thenReturn("blahSettings");
    when(settings.locationLatitude()).thenReturn("blahSettings");
    when(response.getStatus()).thenReturn(404);
    when(httpClient.submitGetRequest(any(), any())).thenReturn(response);

    assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected HTTP status '404' received when getting weather from api");
  }

  private final HttpResponse response = mock(HttpResponse.class);
  private final HttpClient httpClient = mock(HttpClient.class);
  private final TestLogger logger = new TestLogger();
  private final Settings settings = mock(Settings.class);
  private final WeatherClientUnmarshaller unmarshaller = mock(WeatherClientUnmarshaller.class);
  private final WeatherClient weatherClient = new WeatherClient(httpClient, settings, logger, unmarshaller);
}