package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.infrastructure.properties.WeatherApiSettings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

public class WeatherClient {
  private final UnirestHttpClient unirestHttpClient;   // Use interface
  private final WeatherApiSettings settings;
  private final Logger logger;

  public WeatherClient(UnirestHttpClient unirestHttpClient, WeatherApiSettings settings, Logger logger) {
    this.unirestHttpClient = unirestHttpClient;
    this.settings = settings;
    this.logger = logger;
  }

  public int getWeatherId() {
    try {
      HttpResponse<JsonNode> response = unirestHttpClient.submitGetRequest(settings.weatherUrl(),
              settings.locationLongitude(),
              settings.locationLatitude(),
              settings.appId());

      return unmarshallResponse(response);

      // TODO deal with not correct response (response status code not 200)
    } catch (UnirestException e) {
      logger.error("Unexpected exception when getting weather from api", e);
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
