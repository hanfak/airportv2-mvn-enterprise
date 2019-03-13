package com.hanfak.airport.usecase;


import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;
import static com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus.successfulPlaneLandStatus;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LandPlaneUseCaseTest implements WithAssertions {

  @Test
  public void airportInstructsPlaneToLand() {
    when(planeInventoryService.checkPlaneIsAtAirport(flyingPlane.planeId)).thenReturn(false);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(planeInventoryService).addPlane(landedPlane);
    verify(logger).info(eq("Plane, 'A0001', has successfully landed at the airport"));
    assertThat(actionUnderTest.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsNotFlying() {
    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(landedPlane);

    verify(planeInventoryService, never()).addPlane(landedPlane);
    verify(logger).info("Plane, 'A0001', cannot land, status is 'LANDED'");
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForLandedPlane);
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForLandedPlane = failedPlaneLandStatus(planeId("A0001"), LANDED, NOT_IN_AIRPORT, PLANE_IS_LANDED);

  private final PlaneInventoryService planeInventoryService = mock(PlaneInventoryService.class);
  private final Logger logger = mock(Logger.class);
  private final LandPlaneUseCase airport = new LandPlaneUseCase(planeInventoryService, logger);
  private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = plane(planeId("A0001"), LANDED);
}