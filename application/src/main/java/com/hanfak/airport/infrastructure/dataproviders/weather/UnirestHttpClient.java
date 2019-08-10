package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class UnirestHttpClient {
  public HttpResponse<JsonNode> submitGetRequest(String url, Map<String, Object> queryParameters)
          throws UnirestException {
    return Unirest.get(url).queryString(queryParameters).asJson();
  }
}

