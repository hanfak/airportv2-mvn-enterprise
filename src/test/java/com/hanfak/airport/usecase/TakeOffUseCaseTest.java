package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TakeOffUseCaseTest implements WithAssertions {

  @Test
  public void removesPlaneFromAirportWhenInstructToTakeOff() {
    when(hangerService.checkPlaneIsAtAirport(planeId("A0001"))).thenReturn(true);

    PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

    verify(hangerService).removePlane(plane);
    assertThat(actionUnderTest.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
  }

  @Test
  public void planeDoesNotLeaveAirportWhenPlaneIsFlying() {
    when(hangerService.checkPlaneIsAtAirport(planeId("A0001"))).thenReturn(false);

    PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(flyingPlane);

    verify(hangerService, never()).removePlane(plane);
    assertThat(actionUnderTest.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForFlyingPlane);
  }

  private final PlaneInventoryService hangerService = mock(PlaneInventoryService.class);
  private final TakeOffUseCase takeOffUseCase = new TakeOffUseCase(hangerService, mock(Logger.class));
  private final Plane plane = plane(planeId("A0001"), LANDED);
  private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
  private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForFlyingPlane = failedPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, PLANE_IS_FLYING);
  private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
}