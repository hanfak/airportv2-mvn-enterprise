package com.hanfak.airport.infrastructure.dataproviders.weather;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenWeatherMapServiceTest {

  @Test
  public void returnsStormyWeather() {
    when(weatherClient.getWeatherId()).thenReturn(650);
    assertThat(openWeatherMapService.isStormy()).isTrue();
  }

  @Test
  public void returnsNonStormyWeather() {
    when(weatherClient.getWeatherId()).thenReturn(800);
    assertThat(openWeatherMapService.isStormy()).isFalse();
  }

  private final WeatherClient weatherClient = mock(WeatherClient.class);
  private final OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService(weatherClient);
}