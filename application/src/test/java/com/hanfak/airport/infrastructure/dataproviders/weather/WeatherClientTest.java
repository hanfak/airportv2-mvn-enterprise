package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherClientTest {

  @Test
  public void throwExceptionWithNetworkProblem() throws Exception {
    UnirestException cause = new UnirestException("blah");
    when(unirestHttpClient.submitGetRequest(any(), any(), any(), any()))
            .thenThrow(cause);

    Assertions.assertThatThrownBy(weatherClient::getWeatherId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Unexpected exception when getting weather from api")
            .hasCause(cause);
  }

  private final UnirestHttpClient unirestHttpClient = mock(UnirestHttpClient.class);
  private final WeatherClient weatherClient = new WeatherClient(unirestHttpClient);

}