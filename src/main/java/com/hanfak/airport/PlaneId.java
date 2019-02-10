package com.hanfak.airport;

import java.util.Objects;

public class PlaneId {

  private String planeId;

  private PlaneId(String planeId) {
    this.planeId = planeId;
  }

  public static PlaneId planeId(String planeId) {
    return new PlaneId(planeId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlaneId planeId = (PlaneId) o;
    return Objects.equals(planeId, planeId.planeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(planeId);
  }

  @Override
  public String toString() {
    return "PlaneId{" +
            "planeId='" + planeId + '\'' +
            '}';
  }
}
