package learning.webserver.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleWebServerEndToEndTest extends YatspecAcceptanceEndToEndTest {

    @Test
    public void doGetRequest() throws UnirestException {
        whenWeMakeAGetRequestTo("/test");

        thenTheResponseBodyIs("hello");
        andTheResponseStatusCodeIs(200);
    }

    private void whenWeMakeAGetRequestTo(String endpoint) throws UnirestException {
        log("API Url", apiUrl);
        HttpResponse<String> httpResponse = Unirest.get(apiUrl).asString();

        responseStatus = httpResponse.getStatus();
        responseBody = httpResponse.getBody();
        log("Response Status", responseStatus);
    }

    private void thenTheResponseBodyIs(String expectedResponseBody) {
        assertThat(responseBody).isEqualTo(expectedResponseBody);
    }

    private void andTheResponseStatusCodeIs(int expectedStatusCode) {
        assertThat(responseStatus).isEqualTo(expectedStatusCode);
    }

    private int responseStatus;
    private String responseBody;
    private final String apiPath = "/test";
    private final String apiUrl = "http://localhost:1234" + apiPath;
}
