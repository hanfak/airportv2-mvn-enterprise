package com.hanfak.airport.domain.planetakeoffstatus;

public enum TakeOffFailureReason {

  PLANE_IS_FLYING("Plane could not take off as it is still Flying"),
  PLANE_IS_NOT_AT_THE_AIRPORT("Plane could not take off as it is not in the airport"),
  PLANE_COULD_NOT_TAKE_OFF("Plane could not take off, something wrong with system");

  private final String reason;

  TakeOffFailureReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return reason;
  }
}
