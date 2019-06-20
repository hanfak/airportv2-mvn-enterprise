package com.hanfak.airport.domain.planelandstatus;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_IS_STORMY;

public class LandFailureReasonTest implements WithAssertions {

  @Test
  public void returnsStringVersionOfEnum() {
    assertThat(PLANE_IS_LANDED.toString()).isEqualTo("Plane could not land as it is still on land");
    assertThat(PLANE_IS_AT_THE_AIRPORT.toString()).isEqualTo("Plane could not land as it is in the airport");
    assertThat(WEATHER_IS_STORMY.toString()).isEqualTo("Plane could not land as it is stormy weather");
  }
}