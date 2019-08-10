package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.infrastructure.dataproviders.weather.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static java.lang.String.format;

public class WeatherApiHealthCheck implements HealthCheckProbe {
  private final Settings settings;
  private final UnirestHttpClient client;

  public WeatherApiHealthCheck(Settings settings, UnirestHttpClient client) {
    this.settings = settings;
    this.client = client;
  }

  @SuppressWarnings("PMD.AvoidPrintStackTrace") // TODO remove when finished status probe
  @Override
  public ProbeResult probe() {
    try {
      HttpResponse<JsonNode> jsonNodeHttpResponse = client.submitGetRequest(settings.weatherUrl(),
              createQueryDetails());
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

  private Map<String, Object> createQueryDetails() {
    return Stream.of(
            new AbstractMap.SimpleEntry<>("lat", settings.locationLatitude()),
            new AbstractMap.SimpleEntry<>("long", settings.locationLongitude()),
            new AbstractMap.SimpleEntry<>("appid", settings.appId()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
