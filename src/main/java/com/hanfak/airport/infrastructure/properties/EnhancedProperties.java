package com.hanfak.airport.infrastructure.properties;

import org.slf4j.Logger;

import java.util.Properties;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class EnhancedProperties {

    private final Properties properties;
    private final Logger logger;

    public EnhancedProperties(Properties properties, Logger logger) {
        this.properties = properties;
        this.logger = logger;
    }

    public String getPropertyOrDefaultValue(String propertyName, String defaultValue) {
        String property = properties.getProperty(propertyName);
        if (property != null) {
            return property;
        }
        logger.warn(format("The property %s was not set, defaulting to %s", propertyName, defaultValue));
        return defaultValue;
    }

    public String getPropertyOrThrowRuntimeException(String propertyName) {
        String property = properties.getProperty(propertyName);
        if (!isEmpty(property)) {
            return property;
        }
        throw new IllegalStateException(format("The property %s was not set and there is no sensible default. Please set the property otherwise the application will not work properly.", propertyName));
    }
}
