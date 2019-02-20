package acceptancetests;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.dataproviders.AirportHangerService;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.Airport;
import com.hanfak.airport.usecase.HangerService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class TakeOffTest extends TestState implements WithAssertions {
  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneIsLanded();

    whenAPlaneIsInstructedToTakeOff();

    thenthePlaneHasLeftTheAirport();
    andThePlaneIsNotLanded();
  }

  private void andThePlaneIsNotLanded() {
    //assert that plane that has left is flying
  }

  private void thenthePlaneHasLeftTheAirport() {
    assertThat(actionDone).isTrue();
    assertThat(hangerService.planeInventory()).doesNotContain(plane);
  }

  private void whenAPlaneIsInstructedToTakeOff() {
    airport = new Airport(hangerService);
    actionDone = airport.instructPlaneToTakeOff(plane);
  }

  private void givenAPlaneIsLanded() {
    plane = new Plane(planeId("A0001"), LANDED);
    interestingGivens.add("plane", plane);
  }

  private Airport airport;
  private Plane plane;
  private boolean actionDone;     // better variable name
  private HangerService hangerService = new AirportHangerService(); // Should use a stub
}
