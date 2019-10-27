package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.util.Map;

public class UnirestHttpClient implements HttpClient {

  private final HttpClientSettngs settings;

  public UnirestHttpClient(HttpClientSettngs settings) {
    this.settings = settings;
  }

  public HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    return getHttpRequest(url, queryParameters).asJson();
  }

  public HttpRequest getHttpRequest(String url, Map<String, Object> queryParameters) {
    Unirest.setTimeouts(settings.connectionTimeoutSettings(), settings.socketTimeoutSettings());
    return Unirest.get(url).queryString(queryParameters);
  }
}
