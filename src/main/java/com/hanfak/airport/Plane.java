package com.hanfak.airport;

import java.util.Objects;

// Domain
public class Plane {
  public final PlaneId planeId;
  public final PlaneStatus planeStatus;

  public Plane(PlaneId planeId, PlaneStatus planeStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Plane plane = (Plane) o;
    return Objects.equals(planeId, plane.planeId) &&
            planeStatus == plane.planeStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(planeId, planeStatus);
  }

  @Override
  public String toString() {
    return "Plane{" +
            "planeId=" + planeId +
            ", planeStatus=" + planeStatus +
            '}';
  }
}
