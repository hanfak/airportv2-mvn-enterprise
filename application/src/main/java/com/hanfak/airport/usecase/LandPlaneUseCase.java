package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.LandFailureReason;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;
import static com.hanfak.airport.domain.planelandstatus.PlaneLandStatus.createPlaneLandStatus;

// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status
@SuppressWarnings("PMD.TooManyStaticImports") // These all refer to domain objects
public class LandPlaneUseCase {

  private final PlaneInventoryService planeInventoryService;
  private Logger logger;

  public LandPlaneUseCase(PlaneInventoryService hangerService, Logger logger) {
    this.planeInventoryService = hangerService;
    this.logger = logger;
  }

  // What to return for application output, specific type to include plane, status, inAirport and inHanger (later specific hanger)
  public PlaneLandStatus instructPlaneToLand(Plane plane) {
    if (LANDED.equals(plane.planeStatus)) {
      logger.info(String.format("Plane, '%s', cannot land, status is '%s'", plane.planeId, plane.planeStatus.name()));
      return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, PLANE_IS_LANDED);
    }

    // Should this check be here or in planeInventoryService? if in planeInventoryService can throw exception
    try {
      Plane landedPlane = plane.land();
      planeInventoryService.addPlane(landedPlane);
      logger.info(String.format("Plane, '%s', has successfully landed at the airport", plane.planeId));

      return getSuccessfulPlaneLandStatus(landedPlane);
    } catch (Exception e) {
      logger.error(String.format("Plane, '%s', is at airport", plane.planeId), e);
      return getFailurePlaneLandStatus(plane, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
    }
  }

  private PlaneLandStatus getFailurePlaneLandStatus(Plane plane, AirportStatus airportStatus, LandFailureReason landFailureReason) {
    return createPlaneLandStatus(null,
            failedPlaneLandStatus(plane.planeId, plane.planeStatus, airportStatus, landFailureReason));
  }

  private PlaneLandStatus getSuccessfulPlaneLandStatus(Plane landedPlane) {
    return createPlaneLandStatus(SuccessfulPlaneLandStatus.successfulPlaneLandStatus(landedPlane.planeId, landedPlane.planeStatus, IN_AIRPORT)
            , null);
  }
}
