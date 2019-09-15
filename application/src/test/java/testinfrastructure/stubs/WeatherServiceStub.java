package testinfrastructure.stubs;

import com.hanfak.airport.usecase.WeatherService;

public class WeatherServiceStub implements WeatherService {

  private final Boolean isStormy;

  public WeatherServiceStub(Boolean isStormy) {
    this.isStormy = isStormy;
  }

  @Override
  public boolean isStormy() {
    return isStormy;
  }
}
