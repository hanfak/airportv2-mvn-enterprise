package com.hanfak.airport.domain;

public class PlaneId extends SingleValueType<String> {

  private PlaneId(String planeId) {
    super(planeId);
  }

  public static PlaneId planeId(String planeId) {
    return new PlaneId(planeId);
  }
}
