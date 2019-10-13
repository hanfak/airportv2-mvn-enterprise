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
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.*;
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
    // bubble up exception, user will repeat call when no weather returned. This will be handled later via a cache
    try {
      if (weatherService.isStormy()) {
        return weatherNotSuitableForLandingFailureStatus(plane);
      }
    } catch (IllegalStateException e) {
      return weatherSystemFailureStatus(plane, e);
    }

    if (LANDED.equals(plane.planeStatus)) {
      return planeAlreadyLandedFailureStatus(plane);
    }

    Plane landedPlane = plane.land();
    try {
      if (planeInventoryService.planeIsPresentInAirport(landedPlane)) {
        return planeInAirportFailureStatus(plane);
      } else {
        try {
          planeInventoryService.addPlane(landedPlane);
          return planeLandedSuccessStatus(plane, landedPlane);
        } catch (IllegalStateException e) {
          return systemFailureStatus(plane, e);
        }
      }
    } catch (IllegalStateException e) {
      return systemFailureStatus(plane, e);
    }
  }
  // tODO p1: remove nulls, create better output models and mappings
  // https://www.techiedelight.com/return-multiple-values-method-java/
  private PlaneLandStatus weatherSystemFailureStatus(Plane plane, IllegalStateException exception) {
    logger.error("Something went wrong retrieving the weather at the airport", exception);
    return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, WEATHER_NOT_AVAILABLE);
  }

  private PlaneLandStatus planeLandedSuccessStatus(Plane plane, Plane landedPlane) {
    logger.info(format("Plane, '%s', has successfully landed at the airport", plane.planeId));
    return createPlaneLandStatus(successfulPlaneLandStatus(landedPlane.planeId, landedPlane.planeStatus, IN_AIRPORT)
            , null);
  }

  private PlaneLandStatus planeAlreadyLandedFailureStatus(Plane plane) {
    logger.info(format("Plane, '%s', could not land at the airport as it's status is '%s'", plane.planeId, plane.planeStatus.name()));
    return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, PLANE_IS_LANDED);
  }

  private PlaneLandStatus weatherNotSuitableForLandingFailureStatus(Plane plane) {
    logger.info(format("Plane, '%s', could not land at the airport as it is stormy", plane.planeId));
    return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, WEATHER_IS_STORMY);
  }

  private PlaneLandStatus planeInAirportFailureStatus(Plane plane) {
    logger.info(format("Plane, '%s', cannot land, it is at the airport", plane.planeId));
    return getFailurePlaneLandStatus(plane, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
  }

  private PlaneLandStatus systemFailureStatus(Plane plane, IllegalStateException exception) {
    logger.error(format("Something went wrong storing the Plane, '%s', at the airport", plane.planeId), exception);
    return getFailurePlaneLandStatus(plane, NOT_IN_AIRPORT, PLANE_COULD_NOT_LAND);
  }

  private PlaneLandStatus getFailurePlaneLandStatus(Plane plane, AirportStatus airportStatus, LandFailureReason landFailureReason) {
    return createPlaneLandStatus(null,
            failedPlaneLandStatus(plane.planeId, plane.planeStatus, airportStatus, landFailureReason));
  }

}
