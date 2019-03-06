package com.hanfak.airport.domain.planelandstatus;

public enum LandFailureReason {

  PLANE_IS_LANDED("Plane could not land as it is still on land");

  private final String reason;

  LandFailureReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return reason;
  }
}
