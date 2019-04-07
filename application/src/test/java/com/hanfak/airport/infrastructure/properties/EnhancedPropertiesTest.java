package com.hanfak.airport.infrastructure.properties;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import testinfrastructure.TestLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.BDDAssertions.then;


public class EnhancedPropertiesTest implements WithAssertions {

  private final TestLogger logger = new TestLogger();

  @Test
  public void propertyWithEquals() throws IOException {
    Properties properties = new Properties();
    String key = "aProperty";
    properties.load(new ByteArrayInputStream((key + "=a=b=c").getBytes()));

    String propertyOrDefaultValue = new EnhancedProperties(properties, logger).getPropertyOrDefaultValue(key, "666");

    then(propertyOrDefaultValue).isEqualTo("a=b=c");
  }

  @Test
  public void shouldUsePropertyWhenPropertyIsSet() {
    Properties properties = new Properties();
    String key = "aProperty";
    properties.setProperty(key, "555");

    String propertyOrDefaultValue = new EnhancedProperties(properties, logger).getPropertyOrDefaultValue(key, "666");

    then(propertyOrDefaultValue).isEqualTo("555");
  }

  @Test
  public void shouldUseDefaultValueWhenPropertyIsNotSet() {
    Properties properties = new Properties();

    String propertyOrDefaultValue = new EnhancedProperties(properties, logger).getPropertyOrDefaultValue("aProperty", "666");

    then(propertyOrDefaultValue).isEqualTo("666");
    assertThat(logger.warnLogs()).containsExactly("The property aProperty was not set, defaulting to 666");
  }

  @Test
  public void shouldThrowIllegalStateExceptionWhenARequiredPropertyIsNotSet() {
    Properties properties = new Properties();

    try {
      new EnhancedProperties(properties, logger).getPropertyOrThrowRuntimeException("aProperty");
      fail("Exception nextValue");
    } catch (IllegalStateException e) {
      assertThat(e).hasMessage("The property aProperty was not set and there is no sensible default. Please set the property otherwise the application will not work properly.");
    }
  }
}