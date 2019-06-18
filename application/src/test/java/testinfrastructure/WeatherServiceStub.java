package testinfrastructure;

import com.hanfak.airport.usecase.WeatherService;

public class WeatherServiceStub implements WeatherService {

  private final boolean isStormy;

  public WeatherServiceStub(boolean isStormy) {
    this.isStormy = isStormy;
  }

  @Override
  public boolean isStormy() {
    return isStormy;
  }
}
