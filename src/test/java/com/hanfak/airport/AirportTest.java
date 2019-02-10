package com.hanfak.airport;


import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class AirportTest implements WithAssertions {

  @Test
  public void airportHasNoPlanesInHanger() {
    Airport airport = new Airport();
    assertThat(airport.hanger).isEmpty();
  }

  @Test
  public void airportHasAPlaneWhenItInstructsPlaneToLand() {
    Airport airport = new Airport();
    Plane plane = new Plane();
    airport.instructPlaneToLand(plane);
    assertThat(airport.hanger).contains(plane);
  }
}