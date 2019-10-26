package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;
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

@SuppressWarnings("unchecked") // For test
public class LoggingHttpClientTest {

  @Test
  public void shouldLogSuccessfulRequestWithTime() throws Exception {
    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(httpLoggingFormatter.requestOutput(request)).thenReturn("Request String");
    when(timerFactory.startTimer()).thenReturn(timer);
    when(delegate.submitGetRequest(URL, queryParameters)).thenReturn(response);
    when(httpLoggingFormatter.responseOutput(response)).thenReturn("Response String");
    when(timer.elapsedTime()).thenReturn(Duration.ofSeconds(10));
    when(logObfuscator.obfuscateLogs(anyString()))
            .thenReturn("Request from Application to request url\nRequest String")
            .thenReturn("Response from request url to Application received after 0ms\nResponse String");

    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator, httpLoggingFormatter);

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
    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(httpLoggingFormatter.requestOutput(request)).thenReturn("Request String");
    when(timerFactory.startTimer()).thenReturn(timer);
    when(delegate.submitGetRequest(URL, queryParameters)).thenThrow(delegateException);
    when(timer.elapsedTime()).thenReturn(Duration.ofMillis(ELAPSED_MILLISECONDS));

    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator, httpLoggingFormatter);

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

    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(httpLoggingFormatter.requestOutput(request)).thenReturn("Request String");
    when(request.getUrl()).thenReturn("http://api.openweathermap.org/data/2.5/weather?&appid=a_secret1234&lon=-0.454296&lat=51.470020");
    when(timerFactory.startTimer()).thenReturn(timer);
    when(delegate.submitGetRequest(URL, queryParameters)).thenReturn(response);
    when(httpLoggingFormatter.responseOutput(response)).thenReturn("Response String");
    when(timer.elapsedTime()).thenReturn(Duration.ofSeconds(10));
    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator, httpLoggingFormatter);

    HttpResponse<JsonNode> actualResponse = httpClient.submitGetRequest(URL, queryParameters);

    assertThat(response).isSameAs(actualResponse);
    assertThat(logger.infoLogs()).containsExactly("Request from Application to http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020\nRequest String",
            "Response from http://api.openweathermap.org/data/2.5/weather?&appid=******&lon=-0.454296&lat=51.470020 to Application received after 0ms\nResponse String");
  }

  private static final int ELAPSED_MILLISECONDS = 100;
  private static final String URL = "request url";

  private final Map<String, Object> queryParameters = new HashMap<>();
  private final HttpRequest request = mock(HttpRequest.class);
  private final TestLogger logger = new TestLogger();
  private final HttpClient delegate = mock(HttpClient.class);
  private final HttpResponse response = mock(HttpResponse.class);
  private final TimerFactory timerFactory = mock(TimerFactory.class);
  private final Timer timer = mock(Timer.class);
  private final LogObfuscator logObfuscator = mock(LogObfuscator.class);
  private final HttpLoggingFormatter httpLoggingFormatter = mock(HttpLoggingFormatter.class);
}