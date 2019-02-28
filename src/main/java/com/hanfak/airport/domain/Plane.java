package com.hanfak.airport.domain;

// Domain
public class Plane extends ValueType {
  // Add static types for Landed and Flying

  public final PlaneId planeId;
  public final PlaneStatus planeStatus;

  public Plane(PlaneId planeId, PlaneStatus planeStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
  }

  public Plane land() {
    // Check for status is not Landed?? but already checked in usecase, so do i need it?
    return new Plane(this.planeId, PlaneStatus.LANDED);
  }
}
