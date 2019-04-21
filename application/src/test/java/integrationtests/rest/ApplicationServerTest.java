package integrationtests.rest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: BDD language
// TODO: move to end to end as easier to bring up whole app instead of webserver alone
public class ApplicationServerTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void canLandPlaneViaRest() throws UnirestException {
    log("API Url", apiUrl);
    HttpResponse<String> httpResponse = Unirest.post(apiUrl)
            .header("accept", "application/json")
            .body("{\"PlaneId\": \"A199234\", \"PlaneStatus\": \"Flying\"}")
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);

    assertThat(responseStatus).isEqualTo(200);
    assertThat(responseBody).isEqualTo("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"LANDED\",\n" +
            "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
            "}");
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/landAirplane";
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
