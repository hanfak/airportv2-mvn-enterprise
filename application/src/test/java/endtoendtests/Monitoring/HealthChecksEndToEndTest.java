package endtoendtests.Monitoring;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SameParameterValue")
public class HealthChecksEndToEndTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void healthChecksPageChecksAllProbes() throws UnirestException {
    whenUserChecksHealthOfApplication();

    thenTheHealthCheckIsSuccessful();
    andTheHealthCheckResponseHasProbe(withName("Database Connection to 'jdbc:postgresql://.*'"), withStatus("OK"), withDescription("Database test query 'SELECT count(*);' was successful"));
  }

  private void whenUserChecksHealthOfApplication() throws UnirestException {
    log("API Url", apiUrl);
    HttpResponse<String> httpResponse = Unirest.get(apiUrl)
            .header("content-type", "application/json")
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
    log("Response Status", responseStatus);
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

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/health";
  private final String apiUrl = format("http://localhost:5555%s", apiPath);
}
