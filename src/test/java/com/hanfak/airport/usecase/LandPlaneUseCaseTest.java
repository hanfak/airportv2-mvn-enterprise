package com.hanfak.airport.usecase;


import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LandPlaneUseCaseTest implements WithAssertions {

  @Test
  public void airportInstructsPlaneToLand() {
    when(planeInventoryService.checkPlaneIsAtAirport(flyingPlane.planeId)).thenReturn(false);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(planeInventoryService).addPlane(landedPlane);
    assertThat(actionUnderTest.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsNotFlying() {
    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isNull();
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsAtTheAirport() {
    when(planeInventoryService.checkPlaneIsAtAirport(flyingPlane.planeId)).thenReturn(true);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isNull();
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = SuccessfulPlaneLandStatus.successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);

  private final PlaneInventoryService planeInventoryService = mock(PlaneInventoryService.class);
  private final LandPlaneUseCase airport = new LandPlaneUseCase(planeInventoryService);
  private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = plane(planeId("A0001"), LANDED);
}