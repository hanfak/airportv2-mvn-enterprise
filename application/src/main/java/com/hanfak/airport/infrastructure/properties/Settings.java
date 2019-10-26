package com.hanfak.airport.infrastructure.properties;

import com.google.common.annotations.VisibleForTesting;
import com.hanfak.airport.infrastructure.httpclient.HttpClientSettngs;
import com.hanfak.airport.usecase.StatusProbesSettings;
import org.slf4j.Logger;

import java.util.Properties;

@SuppressWarnings("PMD.TooManyMethods") // This is fine for this class
public class Settings implements DatabaseSettings, WeatherApiSettings, StatusProbesSettings, HttpClientSettngs {

  private final EnhancedProperties properties;

  @VisibleForTesting
  public Settings(Properties properties, Logger logger) {
    this.properties = new EnhancedProperties(properties, logger);
  }

  @Override
  public String databaseUrl() {
    return properties.getPropertyOrThrowRuntimeException("database.url");
  }

  @Override
  public String databaseUser() {
    return properties.getPropertyOrThrowRuntimeException("database.user");
  }

  @Override
  public String databasePassword() {
    return properties.getPropertyOrThrowRuntimeException("database.password");
  }

  @Override
  public String databaseConnectTimeout() {
    return properties.getPropertyOrThrowRuntimeException("database.connect.timeout");
  }

  @Override
  public int databaseMaxPoolSize() {
    return Integer.parseInt(properties.getPropertyOrThrowRuntimeException("database.max.pool.size"));
  }

  @Override
  public String locationLatitude() {
    return properties.getPropertyOrThrowRuntimeException("open.weather.param.latitude");
  }

  @Override
  public String locationLongitude() {
    return properties.getPropertyOrThrowRuntimeException("open.weather.param.longitude");
  }

  @Override
  public String weatherUrl() {
    return properties.getPropertyOrThrowRuntimeException("open.weather.service.url");
  }

  @Override
  public String appId() {
    return properties.getPropertyOrThrowRuntimeException("open.weather.param.appid");
  }

  @Override
  public String databaseProbeQuery() {
    return properties.getPropertyOrThrowRuntimeException("database.status.probe.query");
  }

  @Override
  public int cacheDuration() {
    return Integer.parseInt(properties.getPropertyOrThrowRuntimeException("cache.duration.in.seconds"));
  }

  @Override
  public Integer connectionTimeoutSettings() {
    return Integer.parseInt(properties.getPropertyOrThrowRuntimeException("connection.timeout.http.client.in.milliseconds"));
  }

  @Override
  public Integer socketTimeoutSettings() {
    return Integer.parseInt(properties.getPropertyOrThrowRuntimeException("socket.timeout.http.client.in.milliseconds"));
  }
}