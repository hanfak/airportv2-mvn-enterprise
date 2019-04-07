package com.hanfak.airport.domain.planelandstatus;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;

public class LandFailureReasonTest implements WithAssertions {

  @Test
  public void returnsStringVersionOfEnum() {
    assertThat(PLANE_IS_LANDED.toString()).isEqualTo("Plane could not land as it is still on land");
  }
}