package com.hanfak.airport;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class PlaneTest implements WithAssertions {

  @Test
  public void initialStatusOfPlaneIsFlying() {
    Plane plane = new Plane();
    assertThat(plane.isFlying()).isTrue();
  }

  @Test
  public void whenPlaneLandsItIsNotFlying() {
    Plane plane = new Plane();
    plane.land();
    assertThat(plane.isFlying()).isFalse();
  }
}