package com.hanfak.airport.domain;

// Domain
public class Plane extends ValueType {

  public final PlaneId planeId;
  public final PlaneStatus planeStatus;

  public Plane(PlaneId planeId, PlaneStatus planeStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
  }
}
