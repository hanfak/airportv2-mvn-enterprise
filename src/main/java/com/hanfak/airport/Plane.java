package com.hanfak.airport;

// Domain
// be immutable, have id as field
public class Plane {
  private boolean status = true;

  public boolean isFlying() {
    return status;
  }

  public void land() {
    status = false;
  }
}
