package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.infrastructure.dataproviders.weather.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static java.lang.String.format;

public class WeatherApiHealthCheck implements HealthCheckProbe {
  private final Settings settings;
  private final UnirestHttpClient client;

  public WeatherApiHealthCheck(Settings settings, UnirestHttpClient client) {
    this.settings = settings;
    this.client = client;
  }

  @Override
  public ProbeResult probe() {
    try {
      HttpResponse<JsonNode> jsonNodeHttpResponse = client.submitGetRequest(settings.weatherUrl(),
              settings.locationLongitude(),
              settings.locationLatitude(),
              settings.appId());
      if (jsonNodeHttpResponse.getStatus() == 200) {
        return success(name(), "Call to Weather Api was successful");
      }
    } catch (UnirestException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String name() {
    return format("Weather Api Connection to '%s'", settings.weatherUrl());
  }
}
