package com.hanfak.airport.domain.planetakeoffstatus;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;

public class TakeOffFailureReasonTest implements WithAssertions {

  @Test
  public void returnsStringVersionOfEnum() {
    assertThat(PLANE_IS_FLYING.toString()).isEqualTo("Plane could not take off as it is still Flying");
    assertThat(PLANE_IS_NOT_AT_THE_AIRPORT.toString()).isEqualTo("Plane could not take off as it is not in the airport");
  }
}