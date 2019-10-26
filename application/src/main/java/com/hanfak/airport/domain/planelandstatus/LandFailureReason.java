package com.hanfak.airport.domain.planelandstatus;

public enum LandFailureReason {
  PLANE_IS_LANDED("Plane could not land as it is still on land"),
  PLANE_IS_AT_THE_AIRPORT("Plane could not land as it is in the airport"),
  PLANE_COULD_NOT_LAND("Plane could not land at airport, something went wrong with the system"),
  WEATHER_NOT_AVAILABLE("Plane could not land at airport, something went wrong retrieving weather"),
  WEATHER_IS_STORMY("Plane could not land as it is stormy weather");

  private final String reason;

  LandFailureReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return reason;
  }
}
