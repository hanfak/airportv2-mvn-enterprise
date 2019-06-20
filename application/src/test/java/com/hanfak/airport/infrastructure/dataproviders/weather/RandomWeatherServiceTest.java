package com.hanfak.airport.infrastructure.dataproviders.weather;

import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomWeatherServiceTest {

  @Test
  public void returnsStormyWeather() {
    when(random.nextInt(UPPER_BOUND)).thenReturn(8);
    boolean weather = weatherService.isStormy();
    assertThat(weather).isTrue();
  }

  @Test
  public void returnsNonStormyWeather() {
    when(random.nextInt(UPPER_BOUND)).thenReturn(1);
    boolean weather = weatherService.isStormy();
    assertThat(weather).isFalse();
  }

  private static final int UPPER_BOUND = 9;

  private final Random random = mock(Random.class);
  private final RandomWeatherService weatherService = new RandomWeatherService(random);

}