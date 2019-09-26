package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class LoggingHttpClient implements HttpClient {

  private final Logger logger;
  private final HttpClient delegate;
  private final TimerFactory timerFactory;
  private final LogObfuscator logObfuscator;

  public LoggingHttpClient(Logger logger, HttpClient delegate, TimerFactory timerFactory, LogObfuscator logObfuscator) {
    this.logger = logger;
    this.delegate = delegate;
    this.timerFactory = timerFactory;
    this.logObfuscator = logObfuscator;
  }

  @Override
  public HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    return tryToExecuteRequest(url, queryParameters);
  }

  @Override
  public HttpRequest getHttpRequest(String url, Map<String, Object> queryParameters) {
    return delegate.getHttpRequest(url, queryParameters);
  }

  private HttpResponse<JsonNode> tryToExecuteRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    HttpRequest httpRequest = getHttpRequest(url, queryParameters);
    String requestUrl = httpRequest.getUrl();
    logRequest(httpRequest, requestUrl);
    Timer timer = timerFactory.startTimer();
    try {
      HttpResponse<JsonNode> response = delegate.submitGetRequest(url, queryParameters);
      logResponse(requestUrl, timer, response);
      return response;
    } catch (RuntimeException exception) {
      logError(httpRequest, requestUrl, timer, exception);
      throw exception;
    }
  }

  private void logRequest(HttpRequest httpRequest, String requestUrl) {
    String formattedRequest = requestOutput(httpRequest);
    logger.info(logObfuscator.obfuscateLogs(format("Request from Application to %s\n%s", requestUrl, formattedRequest)));
  }

  private void logResponse(String requestUrl, Timer timer, HttpResponse<JsonNode> response) {
    Duration elapsedTime = timer.elapsedTime();
    String formattedResponse = responseOutput(response);
    logger.info(logObfuscator.obfuscateLogs(format("Response from %s to Application received after %dms\n%s", requestUrl, elapsedTime.toMillis(), formattedResponse)));
  }
// TODO P1 extract to delegate
  private String requestOutput(HttpRequest httpRequest) {
    return format("%s %s HTTP/1.1%s", httpRequest.getHttpMethod(), httpRequest.getUrl(), "\n")
            + headersFormatter(httpRequest.getHeaders())
            + "\r\n\r\n"
            + requestBody(httpRequest);
  }

  private String requestBody(HttpRequest httpRequest) {
    return Optional.ofNullable( httpRequest.getBody())
            .map(Object::toString)
            .orElse("");
  }

  private String responseOutput(HttpResponse response) {
    return format("%s %s%n%s%n%n%s", "HTTP", response.getStatus(), headersFormatter(response.getHeaders()), responseBody(response));
  }

  private String responseBody(HttpResponse httpRequest) {
    return Optional.ofNullable( httpRequest.getBody())
            .map(Object::toString)
            .orElse("");
  }

  private String headersFormatter(Map<String, List<String>> headers) {
    return headers.entrySet().stream()
            .map(header -> format("%s: %s", header.getKey(), String.join(",", header.getValue())))
            .collect(joining(lineSeparator()));
  }

  private void logError(HttpRequest httpRequest, String requestUrl, Timer timer, RuntimeException exception) {
    Duration elapsedTime = timer.elapsedTime();
    logger.error(logObfuscator.obfuscateLogs(format("Failed to execute request from Application to %s after %dms\n%s", requestUrl, elapsedTime.toMillis(), httpRequest)), exception);
  }
}
