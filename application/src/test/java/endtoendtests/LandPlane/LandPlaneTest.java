package endtoendtests.LandPlane;

import com.hanfak.airport.domain.plane.Plane;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class LandPlaneTest extends YatspecAcceptanceIntegrationTest {

  @Test // Testing the servlet class
  public void canLandPlaneViaRest() throws UnirestException {
    givenAFlyingPlane();

    whenUserInstructsPlaneToLand();

    thenPlaneHasLandedAndInTheAirport();
  }

  private void givenAFlyingPlane() {
    plane = plane(planeId("A199234"), FLYING);
  }

  private void whenUserInstructsPlaneToLand() throws UnirestException {
    log("API Url", apiUrl);
    HttpResponse<String> httpResponse = Unirest.post(apiUrl)
            .header("accept", "application/json")
            .body(format("{\"PlaneId\": \"%s\", \"PlaneStatus\": \"%s\"}", "A199234", "FLYING"))
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);
  }

  private void thenPlaneHasLandedAndInTheAirport() {
    assertThat(responseStatus).isEqualTo(200);
    assertThat(responseBody).isEqualTo("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"LANDED\",\n" +
            "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
            "}");
  }

  private int responseStatus;
  private String responseBody;
  private Plane plane;
  private final String apiPath = "/landAirplane";
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
