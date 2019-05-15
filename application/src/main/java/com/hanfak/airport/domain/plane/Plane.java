package com.hanfak.airport.domain.plane;

import com.hanfak.airport.domain.helper.ValueType;

import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;

public class Plane extends ValueType {

  public final PlaneId planeId;
  public final PlaneStatus planeStatus;
  // TODO static types for Landed and Flying
  private Plane(PlaneId planeId, PlaneStatus planeStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
  }

  public static Plane plane(PlaneId planeId, PlaneStatus planeStatus) {
    return new Plane(planeId, planeStatus);
  }

  public Plane land() {
    return plane(this.planeId, LANDED);
  }

  public Plane fly() {
    return plane(this.planeId, FLYING);
  }
}
