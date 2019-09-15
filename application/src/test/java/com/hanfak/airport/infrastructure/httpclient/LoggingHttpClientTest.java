package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;
import org.junit.Before;
import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingHttpClientTest {

  private final LogObfuscator logObfuscator = mock(LogObfuscator.class);

  @Test
  public void shouldLogSuccessfulRequestWithTime() throws Exception {
    when(logObfuscator.obfuscateLogs(anyString()))
            .thenReturn("Request from Application to request url\nRequest String")
            .thenReturn("Response from request url to Application received after 0ms\nResponse String");

    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator);

    HttpResponse<JsonNode> actualResponse = httpClient.submitGetRequest(URL, queryParameters);

    assertThat(response).isSameAs(actualResponse);
    assertThat(logger.infoLogs()).containsExactly("Request from Application to request url\nRequest String",
            "Response from request url to Application received after 0ms\nResponse String");
  }

  @Test
  public void shouldLogFailedRequestWithTime() throws Exception {
    when(logObfuscator.obfuscateLogs(anyString()))
            .thenReturn("Request from Application to request url\nRequest String")
            .thenReturn("Failed to execute request from Application to request url after 100ms\nRequest String");
    RuntimeException delegateException = new RuntimeException("Some exception");
    when(delegate.submitGetRequest(URL, queryParameters)).thenThrow(delegateException);
    when(timer.elapsedTime()).thenReturn(Duration.ofMillis(ELAPSED_MILLISECONDS));

    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator);

    try {
      httpClient.submitGetRequest(URL, queryParameters);
      fail("Expected delegate exception to be bubbled up");
    } catch (RuntimeException e) {
      assertThat(e).isEqualTo(delegateException);
      assertThat(logger.errorLogs()).containsExactly("Failed to execute request from Application to request url after 100ms\nRequest String");
      assertThat(logger.errorCauses()).containsExactly(delegateException);
    }
  }

  @Test
  public void shouldObfuscateUsernamesAndPasswords() throws Exception {
    when(logObfuscator.obfuscateLogs(anyString()))
            .thenReturn("Request from Application to http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020\nRequest String")
            .thenReturn("Response from http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020 to Application received after 0ms\nResponse String");

    when(request.getUrl()).thenReturn("http://api.openweathermap.org/data/2.5/weather?&appid=a_secret1234&lon=-0.454296&lat=51.470020");

    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator);

    HttpResponse<JsonNode> actualResponse = httpClient.submitGetRequest(URL, queryParameters);

    assertThat(response).isSameAs(actualResponse);
    assertThat(logger.infoLogs()).containsExactly("Request from Application to http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020\nRequest String",
            "Response from http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020 to Application received after 0ms\nResponse String");
  }

  @Before
  public void setUp() throws Exception {
    when(delegate.submitGetRequest(URL, queryParameters)).thenReturn(response);
    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(request.getUrl()).thenReturn("request url");
    when(request.toString()).thenReturn("Request String");
    when(response.toString()).thenReturn("Response String");
    when(timer.elapsedTime()).thenReturn(time);
    when(timerFactory.startTimer()).thenReturn(timer);
  }

  private static final int ELAPSED_MILLISECONDS = 100;
  private static final String URL = "request url";

  private final Map<String, Object> queryParameters = new HashMap<>();
  private final HttpRequest request = mock(HttpRequest.class);
  private final Duration time = mock(Duration.class);
  private final TestLogger logger = new TestLogger();
  private final HttpClient delegate = mock(HttpClient.class);
  private final HttpResponse response = mock(HttpResponse.class);
  private final TimerFactory timerFactory = mock(TimerFactory.class);
  private final Timer timer = mock(Timer.class);
}