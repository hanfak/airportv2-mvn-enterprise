package com.hanfak.airport.domain;

// Validation over this domain
public class PlaneId extends SingleValueType<String> {

  private PlaneId(String planeId) {
    super(planeId);
  }

  public static PlaneId planeId(String planeId) {
    return new PlaneId(planeId);
  }
}
