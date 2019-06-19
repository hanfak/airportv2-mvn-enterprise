package com.hanfak.airport.domain.planelandstatus;

public enum LandFailureReason {

  PLANE_IS_LANDED("Plane could not land as it is still on land"),
  PLANE_IS_AT_THE_AIRPORT("Plane could not land as it is in the airport"),
  WEATHER_IS_STORMY("blah");

  private final String reason;

  LandFailureReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return reason;
  }
}
