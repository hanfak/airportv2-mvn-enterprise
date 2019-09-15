package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.LandFailureReason;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_COULD_NOT_LAND;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_IS_STORMY;
import static com.hanfak.airport.domain.planelandstatus.PlaneLandStatus.createPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus.successfulPlaneLandStatus;
import static java.lang.String.format;

// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status
@SuppressWarnings("PMD.TooManyStaticImports") // These all refer to domain objects
public class LandPlaneUseCase {

  private final PlaneInventoryService planeInventoryService;
  private final Logger logger;
  private final WeatherService weatherService;

  public LandPlaneUseCase(PlaneInventoryService hangerService, Logger logger, WeatherService weatherService) {
    this.planeInventoryService = hangerService;
    this.logger = logger;
    this.weatherService = weatherService;
  }

  // What to return for application output, specific type to include plane, status, inAirport and inHanger (later specific hanger)
  public PlaneLandStatus instructPlaneToLand(Plane plane) {
    if (weatherService.isStormy()) { // bubble up exception
      logger.info(format("Plane, '%s', could not land at the airport as it is stormy", plane.planeId));
      return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, WEATHER_IS_STORMY);
    }

    // TODO need to check plane is in airport
    if (LANDED.equals(plane.planeStatus)) {
      logger.info(format("Plane, '%s', could not land at the airport as it's status is '%s'", plane.planeId, plane.planeStatus.name()));
      return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, PLANE_IS_LANDED);
    }

    Plane landedPlane = plane.land();
    // TODO try catch on this as this call can have db exception
    if (planeInventoryService.planeIsPresentInAirport(landedPlane)) {
      logger.info(format("Plane, '%s', cannot land, it is at the airport", plane.planeId));
      return getFailurePlaneLandStatus(plane, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
    } else {
      try {
        planeInventoryService.addPlane(landedPlane);
        logger.info(format("Plane, '%s', has successfully landed at the airport", plane.planeId));
        return getSuccessfulPlaneLandStatus(landedPlane);
      } catch (IllegalStateException e) {
        logger.error(format("Something went wrong storing the Plane, '%s', at the airport", plane.planeId), e);
        return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, PLANE_COULD_NOT_LAND);
      }
    }
  }

  private PlaneLandStatus getFailurePlaneLandStatus(Plane plane, AirportStatus airportStatus, LandFailureReason landFailureReason) {
    return createPlaneLandStatus(null,
            failedPlaneLandStatus(plane.planeId, plane.planeStatus, airportStatus, landFailureReason));
  }

  private PlaneLandStatus getSuccessfulPlaneLandStatus(Plane landedPlane) {
    return createPlaneLandStatus(successfulPlaneLandStatus(landedPlane.planeId, landedPlane.planeStatus, IN_AIRPORT)
            , null);
  }
}
