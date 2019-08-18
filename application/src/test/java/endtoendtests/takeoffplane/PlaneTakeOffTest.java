package endtoendtests.takeoffplane;

import com.hanfak.airport.domain.plane.Plane;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class PlaneTakeOffTest extends YatspecAcceptanceIntegrationTest {

  @Test // Testing the servlet class
  public void canTakeOffPlaneViaRest() throws UnirestException {
    givenALandedPlane();

    whenUserInstructsPlaneToTakeOff();

    thenPlaneIsFlyingAndNotInTheAirport();
  }

  private void givenALandedPlane() {
    plane = plane(planeId("A199234"), LANDED);
    airportPlaneInventoryService.addPlane(plane);
  }

  private void whenUserInstructsPlaneToTakeOff() throws UnirestException {
    log("API Url", apiUrl);
    HttpResponse<String> httpResponse = Unirest.post(apiUrl)
            .header("accept", "application/json")
            .body(format("{\"PlaneId\": \"%s\", \"PlaneStatus\": \"%s\"}", "A199234", "LANDED"))
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);
  }

  private void thenPlaneIsFlyingAndNotInTheAirport() {
    assertThat(responseStatus).isEqualTo(200);
    assertThat(responseBody).isEqualTo("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"FLYING\",\n" +
            "  \"AirportStatus\" : \"NOT_IN_AIRPORT\"\n" +
            "}");
  }

  private int responseStatus;
  private String responseBody;
  private Plane plane;
  private final String apiPath = "/takeOffAirplane";
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
