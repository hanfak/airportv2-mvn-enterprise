package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.TestLogger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherClientTest {

  @Test
  public void throwExceptionWithNetworkProblem() throws Exception {
    UnirestException cause = new UnirestException("blah");
    when(unirestHttpClient.submitGetRequest(any(), any(), any(), any()))
            .thenThrow(cause);

    assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected exception when getting weather from api")
            .hasCause(cause);
    assertThat(logger.errorCauses()).contains(cause);
    assertThat(logger.errorLogs()).contains("Unexpected exception when getting weather from api");
  }

  private final UnirestHttpClient unirestHttpClient = mock(UnirestHttpClient.class);
  private final TestLogger logger = new TestLogger();
  private final WeatherClient weatherClient = new WeatherClient(unirestHttpClient, mock(Settings.class), logger);

}