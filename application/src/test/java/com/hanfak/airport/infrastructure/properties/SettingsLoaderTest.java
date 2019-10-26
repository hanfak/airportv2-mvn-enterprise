package com.hanfak.airport.infrastructure.properties;

import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.write;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SettingsLoaderTest implements WithAssertions {

  @Test
  public void overridesPropertiesFromTheSecretFile() throws Exception {
    write(propertiesPath, ("database.url=aUrl\n" +
            "database.user=user\n").getBytes());
    write(secretPropertiesPath, ("database.password=000\n" +
            "database.user=blah\n").getBytes());

    Settings settings = loadSettings(logger, propertiesPath, secretPropertiesPath);

    assertEquals("aUrl", settings.databaseUrl());
    assertEquals("000", settings.databasePassword());
    assertEquals("blah", settings.databaseUser());
  }

  @Test
  public void doesNotLoadSecretPropertiesIfTheFileDoesNotExist() throws Exception {
    write(propertiesPath, "database.url=aUrl".getBytes());

    Settings settings = loadSettings(logger, propertiesPath, Paths.get("/doesNotExist"));

    assertEquals("aUrl", settings.databaseUrl());
  }

  @Test
  public void missingApplicationProperties() {
    assertThatThrownBy(() -> loadSettings(logger, Paths.get("/doesNotExist"), null))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Could not find application properties at '/doesNotExist'");
  }

  private Path propertiesPath;
  private Path secretPropertiesPath;
  private final Logger logger = mock(Logger.class);

  @Before
  public void createTempFiles() throws Exception {
    propertiesPath = createTempFile(getClass().getSimpleName(), ".properties");
    secretPropertiesPath = createTempFile(getClass().getSimpleName() + "-secret", ".properties");
  }

  @After
  public void tearDown() throws Exception {
    delete(propertiesPath);
    delete(secretPropertiesPath);
  }
}