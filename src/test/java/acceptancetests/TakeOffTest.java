package acceptancetests;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.dataproviders.AirportHangerService;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.Airport;
import com.hanfak.airport.usecase.HangerService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class TakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneIsLanded();
    andPlaneIsStoredInTheAirport();

    whenAPlaneIsInstructedToTakeOff();

    thenthePlaneHasLeftTheAirport();
  }

  private void givenAPlaneIsLanded() {
    airport = new Airport(hangerService);
    plane = new Plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andPlaneIsStoredInTheAirport() {
    airport.instructPlaneToLand(plane);
  }

  private void whenAPlaneIsInstructedToTakeOff() {
    actionDone = airport.instructPlaneToTakeOff(new Plane(planeId("A0001"), LANDED));
  }

  private void thenthePlaneHasLeftTheAirport() {
    assertThat(actionDone).isTrue();
    assertThat(hangerService.planeInventory()).doesNotContain(new Plane(planeId("A0001"), LANDED));
  }

  private void andThePlaneIsNotFlying() {
    //assert that plane that has left is flying
  }

  private Airport airport;
  private Plane plane;
  private boolean actionDone;     // better variable name
  private HangerService hangerService = new AirportHangerService(); // Should use a stub
}
