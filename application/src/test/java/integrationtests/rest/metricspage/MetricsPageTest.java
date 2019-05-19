package integrationtests.rest.metricspage;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import testinfrastructure.YatspecAcceptanceIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricsPageTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void metricsPagehowsJvmAndHttpMetrics0() throws UnirestException {
    whenTheMetricsEndpointIsRequested();

    thenTheResponseHasStatusCode(200);
    andTheResponseHasTheJvmMetrics();
    andTheResponseHasTheHttpMetrics();
  }

  private void whenTheMetricsEndpointIsRequested() throws UnirestException {
    log("Request Path", apiUrl);
    HttpResponse<String> httpResponse = Unirest.get(apiUrl)
            .asString();

    responseStatus = httpResponse.getStatus();
    responseBody = httpResponse.getBody();
  }

  @SuppressWarnings("SameParameterValue") // readability
  private void thenTheResponseHasStatusCode(int statusCode) {
    log("Response Status", responseStatus);

    assertThat(responseStatus).isEqualTo(statusCode);
  }

  private void andTheResponseHasTheJvmMetrics() {
    log("Response Body", responseBody);

    assertThat(responseBody).contains("process_cpu_seconds_total");
    assertThat(responseBody).contains("jvm_memory_bytes_used");
    assertThat(responseBody).contains("jvm_memory_bytes_max");
    assertThat(responseBody).contains("vm_threads_current");
    assertThat(responseBody).contains("jvm_threads_peak");
    assertThat(responseBody).contains("jvm_threads_deadlocked");
    assertThat(responseBody).contains("jvm_classes_loaded");
    assertThat(responseBody).contains("jvm_gc_collection_seconds_count");
    assertThat(responseBody).contains("jvm_gc_collection_seconds_sum");
  }

  private void andTheResponseHasTheHttpMetrics() {
    assertThat(responseBody).contains("http_requests_total");
    assertThat(responseBody).contains("http_requests_active");
    assertThat(responseBody).contains("http_requests_active_max");
    assertThat(responseBody).contains("http_request_time_max_seconds");
  }

  private int responseStatus;
  private String responseBody;
  private final String apiPath = "/metrics";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
