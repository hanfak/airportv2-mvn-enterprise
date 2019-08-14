package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.usecase.WeatherService;

import java.util.Random;
// could use this or actual weather api depending on a toggle set in the properties file
// To use in different env ie stage
@Deprecated // Can be used in an environment with no access to third party api
public class RandomWeatherService implements WeatherService {

  private final Random rand;

  public RandomWeatherService(Random rand) {
    this.rand = rand;
  }

  @Override
  public boolean isStormy() {
    return rand.nextInt(9) > 1;
  }

}
