package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.hanfak.airport.infrastructure.httpclient.HttpClient;
import com.hanfak.airport.infrastructure.properties.WeatherApiSettings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class WeatherClient {

  private final HttpClient httpClient;
  private final WeatherApiSettings settings;
  private final Logger logger;
  private final WeatherClientUnmarshaller unmarshaller;

  public WeatherClient(HttpClient httpClient, WeatherApiSettings settings, Logger logger, WeatherClientUnmarshaller unmarshaller) {
    this.httpClient = httpClient;
    this.settings = settings;
    this.logger = logger;
    this.unmarshaller = unmarshaller;
  }

  public int getWeatherId() {
    try {
      HttpResponse<JsonNode> response = httpClient.submitGetRequest(settings.weatherUrl(), createQueryDetails());
      if (response.getStatus() == 200) {
        return unmarshaller.unmarshallWeatherCode(response);
      } else {
        throw new IllegalStateException(format("Unexpected HTTP status '%s' received when getting weather from api", response.getStatus()));
      }
    } catch (UnirestException e) {
      String message = "Unexpected exception when getting weather from api";
      logger.error(message, e); // This is not needed as logs are caught by error handler stage
      throw new IllegalStateException(message, e);
    }
  }

  private Map<String, Object> createQueryDetails() {
    return Stream.of(
            new AbstractMap.SimpleEntry<>("lat", settings.locationLatitude()),
            new AbstractMap.SimpleEntry<>("lon", settings.locationLongitude()),
            new AbstractMap.SimpleEntry<>("appid", settings.appId()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
