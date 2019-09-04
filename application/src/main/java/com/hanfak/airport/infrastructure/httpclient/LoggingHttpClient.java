package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Map;

import static java.lang.String.format;

public class LoggingHttpClient implements HttpClient {

  private static final String OBFUSCATE_APP_ID_REGEX = "appid=.*&lon";
  private static final String OBFUSCATED_APP_ID = "appid=******&lon";

  private final Logger logger;
  private final HttpClient delegate;
  private final TimerFactory timerFactory;

  public LoggingHttpClient(Logger logger, HttpClient delegate, TimerFactory timerFactory) {
    this.logger = logger;
    this.delegate = delegate;
    this.timerFactory = timerFactory;
  }

  @Override
  public HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    return tryToExecuteRequest(url, queryParameters);
  }

  // TODO: Tostring static methods for request and response pretty format in logs, check YatspecFormatters, extract to prod package
  @Override
  public HttpRequest getHttpRequest(String url, Map<String, Object> queryParameters) {
    return delegate.getHttpRequest(url, queryParameters);
  }

  private HttpResponse<JsonNode> tryToExecuteRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    HttpRequest httpRequest = getHttpRequest(url, queryParameters);
    String requestUrl = httpRequest.getUrl();
    logger.info(obfuscateLogs(format("Request from Application to %s\n%s", requestUrl, httpRequest)));
    Timer timer = timerFactory.startTimer();
    try {
      HttpResponse<JsonNode> response = delegate.submitGetRequest(url, queryParameters);
      Duration elapsedTime = timer.elapsedTime();
      logger.info(obfuscateLogs(format("Response from %s to Application received after %dms\n%s", requestUrl, elapsedTime.toMillis(), response)));
      return response;
    } catch (RuntimeException exception) {
      Duration elapsedTime = timer.elapsedTime();
      logger.error(obfuscateLogs(format("Failed to execute request from Application to %s after %dms\n%s", requestUrl, elapsedTime.toMillis(), httpRequest)), exception);
      throw exception;
    }
  }

  // TODO extract as delegate
  private String obfuscateLogs(String log) {
    return log.replaceAll(OBFUSCATE_APP_ID_REGEX, OBFUSCATED_APP_ID);
  }
}
