package com.hanfak.airport.infrastructure.properties;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import testinfrastructure.TestLogger;

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
  public void dataSourceMaxIdleTimeInSeconds() {
    properties.put("database.connect.timeout", "4");
    assertThat(settings.databaseConnectTimeout()).isEqualTo("4");
  }

}