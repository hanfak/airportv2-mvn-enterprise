package com.hanfak.airport.domain.planetakeoffstatus;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class TakeOffFailureReasonTest implements WithAssertions {

  @Test
  public void returnsStringVersionOfEnum() {
    assertThat(TakeOffFailureReason.PLANE_IS_FLYING.toString()).isEqualTo("Plane could not take off as it is still Flying");
  }
}