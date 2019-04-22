package integrationtests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SameParameterValue") // readability
@RunWith(SpecRunner.class)
public class ReadyPageTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void readyPageRespondsWith200() throws UnirestException {
    whenTheReadyPageIsRequested();

    thenTheResponseHasStatusCode(200);
    andTheResponseHasABodyContaining("OK");
  }

  private void whenTheReadyPageIsRequested() throws UnirestException {
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

  @Before
  public void startup() {
    super.application.startWebserver();
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/ready";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
