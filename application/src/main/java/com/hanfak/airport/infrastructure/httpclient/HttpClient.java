package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.util.Map;

public interface HttpClient {
  // TODO make abstract, generic type should be unknown
  // TODO should be a general submitRequest
  HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters) throws UnirestException;
  HttpRequest getHttpRequest(String url, Map<String, Object> queryParameters);
}
