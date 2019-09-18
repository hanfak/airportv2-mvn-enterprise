package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus.createPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_COULD_NOT_TAKE_OFF;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;
import static java.lang.String.format;

@SuppressWarnings("PMD.TooManyStaticImports") // These all refer to domain objects
public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;
  private final Logger logger;

  public TakeOffUseCase(PlaneInventoryService hangerService, Logger logger) {
    this.planeInventoryService = hangerService;
    this.logger = logger;
  }

  // return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Or Throw exception for failure case?? but information will get lost
  // Or Use a map
  // Or Can think about using railway programming??
  public PlaneTakeOffStatus instructPlaneToTakeOff(Plane plane) {
    if (FLYING.equals(plane.planeStatus)) {
      return planeFlyingFailureStatus(plane);
    }

    try {
      if (planeInventoryService.planeIsPresentInAirport(plane)) {
        try {
          planeInventoryService.removePlane(plane);
        } catch (IllegalStateException e) {
          return systemFailureStatus(plane, e);
        }
        Plane flyingPlane = plane.fly();
        return takeOffSuccessStatus(plane, flyingPlane);
      } else {
        return planeNotAtAirportFailureStatus(plane);
      }
    } catch (IllegalStateException e) {
      return systemFailureStatus(plane, e);
    }
  }

  private PlaneTakeOffStatus planeNotAtAirportFailureStatus(Plane plane) {
    logger.info(format("Plane, '%s', cannot take off, it is not at the airport", plane.planeId));
    return createPlaneTakeOffStatus(null, getFailedPlaneTakeOffStatus(plane, PLANE_IS_NOT_AT_THE_AIRPORT, NOT_IN_AIRPORT));
  }

  private PlaneTakeOffStatus takeOffSuccessStatus(Plane plane, Plane flyingPlane) {
    logger.info(format("Plane, '%s', has successfully left the airport", plane.planeId));
    return createPlaneTakeOffStatus(getSuccessfulPlaneTakeOffStatus(flyingPlane), null);
  }

  private PlaneTakeOffStatus systemFailureStatus(Plane plane, IllegalStateException exception) {
    logger.error(format("Something went wrong removing the Plane, '%s', at the airport", plane.planeId), exception);
    return createPlaneTakeOffStatus(null, getFailedPlaneTakeOffStatus(plane, PLANE_COULD_NOT_TAKE_OFF, IN_AIRPORT));
  }

  private PlaneTakeOffStatus planeFlyingFailureStatus(Plane plane) {
    logger.info(format("Plane, '%s', cannot take off, status is '%s', it is not at the airport", plane.planeId, plane.planeStatus.name()));
    return createPlaneTakeOffStatus(null,
            getFailedPlaneTakeOffStatus(plane, PLANE_IS_FLYING, NOT_IN_AIRPORT));
  }

  private SuccessfulPlaneTakeOffStatus getSuccessfulPlaneTakeOffStatus(Plane flyingPlane) {
    return successfulPlaneTakeOffStatus(flyingPlane.planeId, flyingPlane.planeStatus, NOT_IN_AIRPORT);
  }

  private FailedPlaneTakeOffStatus getFailedPlaneTakeOffStatus(Plane plane, TakeOffFailureReason reason, AirportStatus airportStatus) {
    return failedPlaneTakeOffStatus(plane.planeId, plane.planeStatus, airportStatus, reason);
  }
}
