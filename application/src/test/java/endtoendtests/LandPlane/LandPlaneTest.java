package endtoendtests.LandPlane;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.hanfak.airport.domain.plane.Plane;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class LandPlaneTest extends YatspecAcceptanceIntegrationTest {

  private final WireMockServer wireMockServer = new WireMockServer(WIREMOCK_PORT);

  @Test // Testing the servlet class too
  public void canLandPlaneViaRest() throws UnirestException {
    givenAFlyingPlane();
    andTheWeatherIsNotStormy();

    whenUserInstructsPlaneToLand();

    thenPlaneHasLandedAndInTheAirport();
  }

  private void andTheWeatherIsNotStormy() {
    //https://github.com/wojciechbulaty/examples/blob/master/weather-yatspec-example/src/test/java/com/wbsoftwareconsutlancy/WeatherApplicationTest.java
    wireMockServer.start();
    new WireMock(WIREMOCK_PORT)
            // TODO use explicit pattern -  urlEqualTo("/data/2.5/weather?")
            .register(WireMock.any(new UrlPattern(new AnythingPattern(), true))
                    .willReturn(new ResponseDefinitionBuilder()
                            .withBody(weatherServiceOutput)
                            .withStatus(200)));
  }

  private void givenAFlyingPlane() {
    plane = plane(planeId("A199234"), FLYING);
  }

  private void whenUserInstructsPlaneToLand() throws UnirestException {
    log("API Url", appUrl);
    HttpResponse<String> httpResponse = Unirest.post(appUrl)
            .header("content-type", "application/json")
            .body(format("{\"PlaneId\": \"%s\", \"PlaneStatus\": \"%s\"}", "A199234", "FLYING"))
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);
    log("Response body", responseBody);
  }

  private void thenPlaneHasLandedAndInTheAirport() {
    assertThat(responseStatus).isEqualTo(200);
    assertThat(responseBody).isEqualTo("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"LANDED\",\n" +
            "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
            "}");
  }

  @After
  public void teardown() {
    application.stopWebServer();
    wireMockServer.stop();
  }

  private static final int WIREMOCK_PORT = 8888;

  private int responseStatus;
  private String responseBody;
  private Plane plane;
  private final String apiPath = "/landAirplane";
  private final String appUrl = "http://localhost:5555" + apiPath;

  private final String weatherServiceOutput = "{\n" +
          "  \"visibility\": 10000,\n" +
          "  \"timezone\": 3600,\n" +
          "  \"main\": {\n" +
          "    \"temp\": 286.69,\n" +
          "    \"temp_min\": 283.71,\n" +
          "    \"humidity\": 82,\n" +
          "    \"pressure\": 1022,\n" +
          "    \"temp_max\": 289.82\n" +
          "  },\n" +
          "  \"clouds\": {\n" +
          "    \"all\": 29\n" +
          "  },\n" +
          "  \"sys\": {\n" +
          "    \"country\": \"GB\",\n" +
          "    \"sunrise\": 1561175084,\n" +
          "    \"sunset\": 1561234958,\n" +
          "    \"id\": 1417,\n" +
          "    \"type\": 1,\n" +
          "    \"message\": 0.0106\n" +
          "  },\n" +
          "  \"dt\": 1561186731,\n" +
          "  \"coord\": {\n" +
          "    \"lon\": -0.45,\n" +
          "    \"lat\": 51.47\n" +
          "  },\n" +
          "  \"weather\": [\n" +
          "    {\n" +
          "      \"icon\": \"50d\",\n" +
          "      \"description\": \"mist\",\n" +
          "      \"main\": \"Mist\",\n" +
          "      \"id\": 701\n" +
          "    }\n" +
          "  ],\n" +
          "  \"name\": \"Heathrow\",\n" +
          "  \"cod\": 200,\n" +
          "  \"id\": 7284876,\n" +
          "  \"base\": \"stations\",\n" +
          "  \"wind\": {\n" +
          "    \"deg\": 150,\n" +
          "    \"speed\": 1.5\n" +
          "  }\n" +
          "}";
}
