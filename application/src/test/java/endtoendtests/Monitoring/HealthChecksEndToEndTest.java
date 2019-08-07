package endtoendtests.Monitoring;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SameParameterValue") // For readability
public class HealthChecksEndToEndTest extends YatspecAcceptanceIntegrationTest {

  private final WireMockServer wireMockServer = new WireMockServer(8888);

  @Test
  public void healthChecksPageChecksAllProbes() throws UnirestException {
    givenAllStatusProbesAreSuccessful();

    whenUserChecksHealthOfApplication();

    thenTheHealthCheckIsSuccessful();
    andTheHealthCheckResponseHasProbe(withName("Database Connection to 'jdbc:postgresql://.*'"), withStatus("OK"), withDescription("Database test query 'SELECT count(*);' was successful"));
    andTheHealthCheckResponseHasProbe(withName("Weather Api Connection to 'http://localhost:8888/data/2.5/weather?.*'"), withStatus("OK"), withDescription("Call to Weather Api was successful"));
  }

  // TODO one probe is failing = overal status is failing

  // TODO one probe is warning = overal status is warning

  private void givenAllStatusProbesAreSuccessful() {
    theWeatherApiRespondsSuccessfully();
  }

  private void theWeatherApiRespondsSuccessfully() {
    wireMockServer.start();
    new WireMock(8888)
            .register(WireMock.any(new UrlPattern(new AnythingPattern(), true))
                    .willReturn(new ResponseDefinitionBuilder()
                            .withBody("")
                            .withStatus(200)));
  }

  private void whenUserChecksHealthOfApplication() throws UnirestException {
    log("API Url", apiUrl);
    HttpResponse<String> httpResponse = Unirest.get(apiUrl)
            .header("content-type", "application/json")
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);
    log("Response Body", responseBody);
  }

  private void thenTheHealthCheckIsSuccessful() {
    assertThat(responseStatus).isEqualTo(200);
  }

  private void andTheHealthCheckResponseHasProbe(String probeName, String status, String descriptionRegex) {
    JSONArray probes = new JSONObject(responseBody).getJSONArray("probes");
    Assertions.assertThat(probes)
            .describedAs(responseBody)
            .areAtLeastOne(probeMatching(probeName, status, descriptionRegex));
  }

  private String withDescription(String description) {
    return description;
  }

  private String withStatus(String status) {
    return status;
  }

  private String withName(String name) {
    return name;
  }

  private Condition<Object> probeMatching(String probeName, String status, String descriptionRegex) {
    String expectedProbe = probeJson(probeName, status, descriptionRegex);
    return new Condition<>(jsonElement -> probeMatching(probeName, status, descriptionRegex, (JSONObject) jsonElement), expectedProbe);
  }

  private boolean probeMatching(String probeName, String status, String descriptionRegex, JSONObject actualProbe) {
    boolean nameMatches = actualProbe.getString("name").matches(probeName);
    boolean descriptionMatches = actualProbe.getString("description").equals(descriptionRegex);
    boolean statusMatches = actualProbe.getString("status").equals(status);
    return nameMatches && descriptionMatches && statusMatches;
  }

  private String probeJson(String probeName, String status, String descriptionRegex) {
    JSONObject probe = new JSONObject();
    probe.put("name", probeName);
    probe.put("description", descriptionRegex);
    probe.put("status", status);
    return probe.toString();
  }

  @After
  public void teardown() {
    application.stopWebServer();
    wireMockServer.stop();
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/health";
  private final String apiUrl = format("http://localhost:5555%s", apiPath);
}
