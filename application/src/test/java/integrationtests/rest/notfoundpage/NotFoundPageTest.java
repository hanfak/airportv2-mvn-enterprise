package integrationtests.rest.notfoundpage;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

public class NotFoundPageTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void notFoundPageRespondsWith404() throws UnirestException {
    whenANonAvailableEndpointIsRequested();

    thenTheResponseHasStatusCode(404);
    andTheResponseHasABodyContaining("Not available");
  }

  private void whenANonAvailableEndpointIsRequested() throws UnirestException {
    log("Request Path", apiUrl);
    HttpResponse<String> httpResponse = Unirest.get(apiUrl)
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
  }

  private void thenTheResponseHasStatusCode(int statusCode) {
    log("Response Status", responseStatus);

    assertThat(responseStatus).isEqualTo(statusCode);
  }

  private void andTheResponseHasABodyContaining(String body) {
    log("Response Body", responseBody);

    assertThat(responseBody).isEqualTo(body);
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/blah";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555/airport" + apiPath;
}
