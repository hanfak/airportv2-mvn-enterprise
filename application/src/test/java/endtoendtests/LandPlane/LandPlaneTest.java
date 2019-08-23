package endtoendtests.LandPlane;

import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.hanfak.airport.domain.plane.Plane;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import endtoendtests.givens.WeatherSeviceServerStub;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceEndToEndTest;

import java.util.HashMap;
import java.util.Map;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static testinfrastructure.yatspec.YatspecFormatters.requestOutput;
import static testinfrastructure.yatspec.YatspecFormatters.responseOutput;


public class LandPlaneTest extends YatspecAcceptanceEndToEndTest {

  @Test // Testing the servlet class too
  public void canLandPlaneViaRest() throws Exception {
    givenAFlyingPlane();
    and(theWeatherIsNotStormy());

    when(userInstructsPlaneToLand());

    thenPlaneHasLandedAndInTheAirport();
  }

  private void givenAFlyingPlane() {
    plane = plane(planeId("A199234"), FLYING);
  }

  private GivensBuilder theWeatherIsNotStormy() {
    registerListener();
    return interestingGivens -> {
      weatherServiceStub.setupStubbedWeatherResponse();
      return interestingGivens;
    };
  }

  private ActionUnderTest userInstructsPlaneToLand() {
    return (interestingGivens, capturedInputAndOutputs) -> whenUserInstructsPlaneToLand(capturedInputAndOutputs);
  }

  // TODO: extract to whens package, have to deal with fiels, need to use teststate
  private CapturedInputAndOutputs whenUserInstructsPlaneToLand(CapturedInputAndOutputs capturedInputAndOutputs) throws UnirestException {
    HttpResponse<String> response = makeLandPlaneRequest(capturedInputAndOutputs);
    responseStatus = response.getStatus();
    responseBody = response.getBody();
    return capturedInputAndOutputs;
  }

  private HttpResponse<String> makeLandPlaneRequest(CapturedInputAndOutputs capturedInputAndOutputs) throws UnirestException {
    createLandPlaneRequest(capturedInputAndOutputs, stringRequest());
    HttpResponse<String> httpResponse = Unirest.post(APP_URL)
            .headers(headers)
            .body(body)
            .asString();
    String response = responseOutput(responseStatus, httpResponse.getHeaders(), responseBody);
    capturedInputAndOutputs.add(format("Response from %s to %s", APPLICATION_NAME, "User"), response);
    return httpResponse;
  }

  private void createLandPlaneRequest(CapturedInputAndOutputs capturedInputAndOutputs, String request) {
    capturedInputAndOutputs.add(format("Request from %s to %s", "User", APPLICATION_NAME), request);
  }

  private String stringRequest() {
    body = format("{\"PlaneId\": \"%s\", \"PlaneStatus\": \"%s\"}", plane.planeId, plane.planeStatus);
    headers.put("content-type", "application/json");
    return requestOutput(APP_URL, headers, body);
  }

  //TODO: use state extractor
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
  private String body;
  private Map<String, String> headers = new HashMap<>();
  private Plane plane;

  private final WeatherSeviceServerStub weatherServiceStub = new WeatherSeviceServerStub();
  private final String API_PATH = "/landAirplane";
  private final String APP_URL = "http://localhost:5555" + API_PATH;
}
