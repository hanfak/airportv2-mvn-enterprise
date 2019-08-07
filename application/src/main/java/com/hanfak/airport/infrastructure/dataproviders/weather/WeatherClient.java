package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherClient {
  // Use interface
  private final UnirestHttpClient unirestHttpClient;

  public WeatherClient(UnirestHttpClient unirestHttpClient) {
    this.unirestHttpClient = unirestHttpClient;
  }

  public int getWeatherId() {
    try {
      // TODO: Use a map, or properties
      // TODO Add to properties§
      String locationLatitude = "51.470020";
      String locationLongitude = "-0.454296";
      String url = "http://api.openweathermap.org/data/2.5/weather?";
      String appId = "42f829d2049915097be4c996d1275d8d";

      HttpResponse<JsonNode> response = unirestHttpClient.submitGetRequest(url, locationLongitude, locationLatitude, appId);

      return unmarshallResponse(response);

      // TODO deal with not correct response (response status code not 200)
    } catch (UnirestException e) {
      // TODO Logging & delegate
      throw new IllegalStateException("Unexpected exception when getting weather from api", e);
    }
  }

  // TODO: extract out unmarshaller
  private int unmarshallResponse(HttpResponse<JsonNode> response) {
    JSONObject body = response.getBody().getObject();
    JSONArray jArray = body.getJSONArray("weather");
    JSONObject jsonObject = jArray.getJSONObject(0);
    return jsonObject.getInt("id");
  }
}
