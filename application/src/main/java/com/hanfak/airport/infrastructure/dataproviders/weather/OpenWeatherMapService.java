package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.usecase.WeatherService;

public class OpenWeatherMapService implements WeatherService {
  private final WeatherClient weatherClient;

  public OpenWeatherMapService(WeatherClient weatherClient) {
    this.weatherClient = weatherClient;
  }

  @Override
  public boolean isStormy() {
    return weatherClient.getWeatherId() < 700; // Choose better code to use https://openweathermap.org/weather-conditions
  }
}
