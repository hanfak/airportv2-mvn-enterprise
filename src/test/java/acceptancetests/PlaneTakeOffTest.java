package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.PlaneInventoryService;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

// split to happy and sad path tests
@RunWith(SpecRunner.class)
public class PlaneTakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneHasLanded();
    andPlaneIsInTheAirport();

    whenAPlaneIsInstructedToTakeOff();

    thenThePlaneHasLeftTheAirport();
    andThePlaneIsFlying();
  }

  @Test
  public void aPlaneCannotTakeOffIfNotAtAirport() {
    givenAPlaneHasLandedSomewhereOutsideTheAirport();

    whenAPlaneNotInTheAirportIsInstructedToTakeOff();

    thenThereIsAFailureInstructingThePlaneToTakeOff();
  }

  private void givenAPlaneHasLandedSomewhereOutsideTheAirport() {
    takeOffUseCase = new TakeOffUseCase(hangerService, logger);
    plane = plane(planeId("A0001"), LANDED);
    interestingGivens.add("plane", plane);
  }

  private void givenAPlaneHasLanded() {
    takeOffUseCase = new TakeOffUseCase(hangerService, logger);
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andPlaneIsInTheAirport() {
    landPlaneUseCase.instructPlaneToLand(plane);
  }

  private void whenAPlaneIsInstructedToTakeOff() {
    whenAPlaneNotInTheAirportIsInstructedToTakeOff();
  }

  private void whenAPlaneNotInTheAirportIsInstructedToTakeOff() {
    planeTakeOffStatus = takeOffUseCase.instructPlaneToTakeOff(plane(planeId("A0001"), LANDED));
  }

  private void thenThereIsAFailureInstructingThePlaneToTakeOff() {
    verify(logger).info(eq("Plane not at airport"), any(IllegalStateException.class));
    assertThat(planeTakeOffStatus.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForNotPresentPlane);
  }

  private void thenThePlaneHasLeftTheAirport() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    assertThat(hangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void andThePlaneIsFlying() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus.planeStatus).isEqualTo(FLYING);
  }

  //  private final Logger logger = LoggerFactory.getLogger("application");
  private final Logger logger = mock(Logger.class);
  private final PlaneInventoryService hangerService = new AirportPlaneInventoryService(); // Should use a stub
  private final LandPlaneUseCase landPlaneUseCase = new LandPlaneUseCase(hangerService);
  private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
  private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForNotPresentPlane = failedPlaneTakeOffStatus(planeId("A0001"), LANDED, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT);
  private TakeOffUseCase takeOffUseCase;
  private PlaneTakeOffStatus planeTakeOffStatus;
  private Plane plane;
}
