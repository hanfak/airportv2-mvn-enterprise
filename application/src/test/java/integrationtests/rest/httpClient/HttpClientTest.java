package integrationtests.rest.httpClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.hanfak.airport.infrastructure.httpclient.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpClientTest {

  @Test
  public void httpClientShouldSendRequestAndReceivePrimedResponseFromStubServer() throws UnirestException {
    when(settings.connectionTimeoutSettings()).thenReturn(2000);
    when(settings.socketTimeoutSettings()).thenReturn(2000);
    HashMap<String, Object> queryParams = new HashMap<>();

    HttpResponse<JsonNode> response = client.submitGetRequest(appUrl, queryParams);

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getBody().toString()).isEqualTo(weatherServiceOutput);
  }

  private void andTheApiResponds() {
    wireMockServer = new WireMockServer(WIREMOCK_PORT);
    wireMockServer.start();
    new WireMock(WIREMOCK_PORT)
            .register(WireMock.any(new UrlPattern(new AnythingPattern(), true))
                    .willReturn(new ResponseDefinitionBuilder()
                            .withBody(weatherServiceOutput)
                            .withStatus(200)));
  }

  @Before
  public void setUp() {
    andTheApiResponds();
  }

  @After
  public void tearDown() {
    wireMockServer.stop();
  }

  private static final int WIREMOCK_PORT = 1234;
  private final String weatherServiceOutput = "{" +
          "\"PlaneId\":\"A199234\"" +
          "}";
  private WireMockServer wireMockServer;
  private final Settings settings = mock(Settings.class);
  private final UnirestHttpClient client = new UnirestHttpClient(settings);
  private final String apiPath = "/landAirplane";
  private final String appUrl = "http://localhost:1234" + apiPath;
}
