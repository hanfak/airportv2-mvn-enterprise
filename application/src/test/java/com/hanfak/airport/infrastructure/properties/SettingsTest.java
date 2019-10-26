package com.hanfak.airport.infrastructure.properties;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import java.util.Properties;

public class SettingsTest implements WithAssertions {

  private final Properties properties = new Properties();
  private final TestLogger logger = new TestLogger();
  private final Settings settings = new Settings(properties, logger);

  @Test
  public void databaseProperties() {
    properties.put("database.url", "some database url");
    properties.put("database.user", "some database user");
    properties.put("database.password", "some database password");

    assertThat(settings.databaseUrl()).isEqualTo("some database url");
    assertThat(settings.databaseUser()).isEqualTo("some database user");
    assertThat(settings.databasePassword()).isEqualTo("some database password");
  }

  @Test
  public void weatherApiProperties() {
    properties.put("open.weather.service.url", "some url");
    properties.put("open.weather.param.longitude", "some longitude");
    properties.put("open.weather.param.latitude", "some latitude");
    properties.put("open.weather.param.appid", "some appid");

    assertThat(settings.weatherUrl()).isEqualTo("some url");
    assertThat(settings.locationLatitude()).isEqualTo("some latitude");
    assertThat(settings.locationLongitude()).isEqualTo("some longitude");
    assertThat(settings.appId()).isEqualTo("some appid");
  }

  @Test
  public void dataSourceMaxIdleTimeInSeconds() {
    properties.put("database.connect.timeout", "4");
    assertThat(settings.databaseConnectTimeout()).isEqualTo("4");
  }

  @Test
  public void databaseMaxPoolSize() {
    properties.put("database.max.pool.size", "1");
    assertThat(settings.databaseMaxPoolSize()).isEqualTo(1);
  }

  @Test
  public void statusProbeProperties() {
    properties.put("database.status.probe.query", "some query");
    properties.put("cache.duration.in.seconds", "1");

    assertThat(settings.databaseProbeQuery()).isEqualTo("some query");
    assertThat(settings.cacheDuration()).isEqualTo(1);
  }

  @Test
  public void httpClientProperties() {
    properties.put("connection.timeout.http.client.in.milliseconds", "10");
    properties.put("socket.timeout.http.client.in.milliseconds", "20");

    assertThat(settings.socketTimeoutSettings()).isEqualTo(20);
    assertThat(settings.connectionTimeoutSettings()).isEqualTo(10);
  }
}