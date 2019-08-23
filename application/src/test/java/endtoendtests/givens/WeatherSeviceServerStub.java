package endtoendtests.givens;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import static testinfrastructure.YatspecAcceptanceEndToEndTest.WIREMOCK_PORT;

public class WeatherSeviceServerStub {
  public void setupStubbedWeatherResponse() {
    new WireMock(WIREMOCK_PORT)
            .register(WireMock.any(new UrlPattern(new AnythingPattern(), true))
                    .willReturn(WireMock.aResponse()
                            .withBody(WEATHER_SERVICE_RESPONSE_BODY)
                            .withStatus(200)));
  }

  @SuppressWarnings("FieldCanBeLocal")
  private final String WEATHER_SERVICE_RESPONSE_BODY = "{\n" +
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
