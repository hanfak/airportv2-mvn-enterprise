package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.util.Map;

// TODO this class test with a wire mock server up, as a intergration/module test
public class UnirestHttpClient implements HttpClient {

  public HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters) throws UnirestException {
    return getHttpRequest(url, queryParameters).asJson();
  }

  public HttpRequest getHttpRequest(String url, Map<String, Object> queryParameters) {
    // TODO set timeouts Unirest.setTimeouts(60000, 30000);
    return Unirest.get(url).queryString(queryParameters);
  }
}

