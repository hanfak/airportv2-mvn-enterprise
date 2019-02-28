package acceptancetests;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.PlaneInventoryService;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class PlaneTakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneIsLanded();
    andPlaneIsStoredInTheAirport();

    whenAPlaneIsInstructedToTakeOff();

    thenthePlaneHasLeftTheAirport();
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
    takeOffUseCase.instructPlaneToTakeOff(new Plane(planeId("A0001"), LANDED));
  }

  private void thenthePlaneHasLeftTheAirport() {
    assertThat(hangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void andThePlaneIsNotFlying() {
    //assert that plane that has left is flying
  }

  private PlaneInventoryService hangerService = new AirportPlaneInventoryService(); // Should use a stub
  private LandPlaneUseCase landPlaneUseCase = new LandPlaneUseCase(hangerService);
  private TakeOffUseCase takeOffUseCase;
  private Plane plane;
}
