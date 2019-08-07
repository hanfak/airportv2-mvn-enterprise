package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestHttpClient {
  // TODO test via integration test
  HttpResponse<JsonNode> submitGetRequest(String url, String locationLongitude, String locationLatitude, String appId) throws UnirestException {
    ImmutableMap<String, Object> queryParameters = ImmutableMap.of("lat", locationLatitude,
            "lon", locationLongitude,
            "appid", appId);
    return Unirest.get(url).queryString(queryParameters).asJson();
  }
}