package com.hanfak.airport;

import java.util.ArrayList;
import java.util.List;

// Usecase
public class Airport {

  public final List<Plane> hanger = new ArrayList<>(); // separate service

  // return boolean to confirm plane has landed, and in airport
  public void instructPlaneToLand(Plane plane) {
    plane.land();
    hanger.add(plane);
  }
}
