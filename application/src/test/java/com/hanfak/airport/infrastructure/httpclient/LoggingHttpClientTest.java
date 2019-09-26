package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.Body;
import org.apache.http.HttpEntity;
import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO refactor, put in before block
public class LoggingHttpClientTest {

  @Test
  public void shouldLogSuccessfulRequestWithTime() throws Exception {
    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(request.getUrl()).thenReturn("request url").thenReturn("request url");
    when(request.getHttpMethod()).thenReturn(httpMethod);
    when(httpMethod.toString()).thenReturn("GET");
    when(request.getHeaders()).thenReturn(headers);
    when(headers.entrySet()).thenReturn(Collections.emptySet());
    when(request.getBody()).thenReturn(body);
    when(body.getEntity()).thenReturn(entity);
    when(entity.toString()).thenReturn("Request Body");
    when(timerFactory.startTimer()).thenReturn(timer);
    when(delegate.submitGetRequest(URL, queryParameters)).thenReturn(response);
    when(timer.elapsedTime()).thenReturn(Duration.ofSeconds(10));
    when(response.getBody()).thenReturn("Response Body"); //??
    when(response.getStatus()).thenReturn(200);
    when(response.getHeaders()).thenReturn(headers);
    when(headers.entrySet()).thenReturn(Collections.emptySet());
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
    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(request.getUrl()).thenReturn("request url").thenReturn("request url");
    when(request.getHttpMethod()).thenReturn(httpMethod);
    when(httpMethod.toString()).thenReturn("GET");
    when(request.getHeaders()).thenReturn(headers);
    when(headers.entrySet()).thenReturn(Collections.emptySet());
    when(request.getBody()).thenReturn(body);
    when(body.getEntity()).thenReturn(entity);
    when(entity.toString()).thenReturn("Request Body");
    when(timerFactory.startTimer()).thenReturn(timer);
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

    when(delegate.getHttpRequest(URL, queryParameters)).thenReturn(request);
    when(request.getUrl()).thenReturn("http://api.openweathermap.org/data/2.5/weather?&appid=a_secret1234&lon=-0.454296&lat=51.470020");
    when(request.getHttpMethod()).thenReturn(httpMethod);
    when(httpMethod.toString()).thenReturn("GET");
    when(request.getHeaders()).thenReturn(headers);
    when(headers.entrySet()).thenReturn(Collections.emptySet());
    when(request.getBody()).thenReturn(body);
    when(body.getEntity()).thenReturn(entity);
    when(entity.toString()).thenReturn("Request Body");
    when(timerFactory.startTimer()).thenReturn(timer);
    when(delegate.submitGetRequest(URL, queryParameters)).thenReturn(response);
    when(timer.elapsedTime()).thenReturn(Duration.ofSeconds(10));
    when(response.getBody()).thenReturn("Response Body"); //??
    when(response.getStatus()).thenReturn(200);
    when(response.getHeaders()).thenReturn(headers);
    when(headers.entrySet()).thenReturn(Collections.emptySet());
    HttpClient httpClient = new LoggingHttpClient(logger, delegate, timerFactory, logObfuscator);

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
  private final Headers headers = mock(Headers.class);
  private final HttpMethod httpMethod = mock(HttpMethod.class);
  private final LogObfuscator logObfuscator = mock(LogObfuscator.class);
  private final Body body = mock(Body.class);
  private final HttpEntity entity = mock(HttpEntity.class);
}