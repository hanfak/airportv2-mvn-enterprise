package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

public class UnirestHttpClient {
  public HttpResponse<JsonNode> submitGetRequest(String url, String locationLongitude, String locationLatitude, String appId) throws UnirestException {
    Map<String, Object> queryParameters = new HashMap<>();
    queryParameters.put("lat", locationLatitude);
    queryParameters.put("lon", locationLongitude);
    queryParameters.put("appid", appId);
    return Unirest.get(url).queryString(queryParameters).asJson();
  }
}