package endtoendtests.LandPlane;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.hanfak.airport.domain.plane.Plane;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceBlahTest;

import java.util.HashMap;
import java.util.Map;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;


public class LandPlane2Test extends YatspecAcceptanceBlahTest {

  @Test // Testing the servlet class too
  public void canLandPlaneViaRest() throws Exception {
    givenAFlyingPlane();
    andTheWeatherIsNotStormy();

    when(userInstructsPlaneToLand());

    thenPlaneHasLandedAndInTheAirport();
  }

  private void givenAFlyingPlane() {
    plane = plane(planeId("A199234"), FLYING);
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

  private ActionUnderTest userInstructsPlaneToLand() {
    return (interestingGivens, capturedInputAndOutputs) -> whenUserInstructsPlaneToLand(capturedInputAndOutputs);
  }

  private CapturedInputAndOutputs whenUserInstructsPlaneToLand(CapturedInputAndOutputs capturedInputAndOutputs) throws UnirestException {
    body = format("{\"PlaneId\": \"%s\", \"PlaneStatus\": \"%s\"}", "A199234", "FLYING");
    headers.put("content-type", "application/json");
    String request = requestOutput(appUrl, formatter(headers), body);
    capturedInputAndOutputs.add(format("Request from %s to %s", "User", "Server"), request);
    HttpResponse<String> httpResponse = Unirest.post(appUrl)
            .headers(headers)
            .body(body)
            .asString();
    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    responseHeaders = httpResponse.getHeaders();
    capturedInputAndOutputs.add(format("Response from %s to %s", "Server", "User"), responseOutput(responseStatus, responseHeaders, responseBody));
    return capturedInputAndOutputs;
  }

  private String formatter(Map<String, String> headers) {
    return headers.entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), s.getValue()))
            .collect(joining(lineSeparator()));
  }

  public String requestOutput(String uri, String headers, String body) {
    return String.format("%s %s HTTP/1.1%s", "POST", uri, "\r\n") + headers + "\r\n\r\n" + body;
  }

  // TODO format headers
  public String responseOutput(int responseStatus, Headers responseHeaders, String responseBody) {
    return String.format("%s %s%n%s%n%n%s", "HTTP", responseStatus, responseHeaders, responseBody);
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
    capturedInputAndOutputs.add("Sequence Diagram", super.generateSequenceDiagram());
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
  private final WireMockServer wireMockServer = new WireMockServer(WIREMOCK_PORT);
  private String body;
  private Map<String, String> headers = new HashMap<>();
  private Headers responseHeaders;
}
