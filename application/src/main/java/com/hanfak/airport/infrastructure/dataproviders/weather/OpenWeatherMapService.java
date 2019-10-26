package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.usecase.WeatherService;

public class OpenWeatherMapService implements WeatherService {
  private final WeatherClient weatherClient;

  public OpenWeatherMapService(WeatherClient weatherClient) {
    this.weatherClient = weatherClient;
  }

  @Override
  public boolean isStormy() {
    // This is very arbitary, this can have a complex algorithm depending on several codes etc
    return weatherClient.getWeatherId() < 700;
  }
}
