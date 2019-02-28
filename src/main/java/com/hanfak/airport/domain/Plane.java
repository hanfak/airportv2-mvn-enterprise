package com.hanfak.airport.domain;

import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

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
    // Check for status is not Landed, through assertion?? but already checked in usecase, so do i need it?
    return new Plane(this.planeId, LANDED);
  }

  public Plane fly() {
    return new Plane(this.planeId, FLYING);
  }
}
