package acceptancetests;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneTakeOffStatus;
import com.hanfak.airport.domain.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.PlaneInventoryService;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;

public class PlaneTakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneIsLanded();
    andPlaneIsStoredInTheAirport();

    whenAPlaneIsInstructedToTakeOff();

    thenthePlaneHasLeftTheAirport();
    andThePlaneIsFlying();
  }

  private void givenAPlaneIsLanded() {
    takeOffUseCase = new TakeOffUseCase(hangerService);
    plane = new Plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andPlaneIsStoredInTheAirport() {
    landPlaneUseCase.instructPlaneToLand(plane);
  }

  private void whenAPlaneIsInstructedToTakeOff() {
    planeTakeOffStatus = takeOffUseCase.instructPlaneToTakeOff(new Plane(planeId("A0001"), LANDED));
  }

  private void thenthePlaneHasLeftTheAirport() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    assertThat(hangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void andThePlaneIsFlying() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus.planeStatus).isEqualTo(FLYING);
  }


  private final PlaneInventoryService hangerService = new AirportPlaneInventoryService(); // Should use a stub
  private final LandPlaneUseCase landPlaneUseCase = new LandPlaneUseCase(hangerService);
  private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
  private TakeOffUseCase takeOffUseCase;
  private PlaneTakeOffStatus planeTakeOffStatus;
  private Plane plane;
}
