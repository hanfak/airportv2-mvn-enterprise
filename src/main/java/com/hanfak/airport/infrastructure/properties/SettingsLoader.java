package com.hanfak.airport.infrastructure.properties;


import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static java.lang.String.format;

public class SettingsLoader {
    public static Settings loadSettings(Logger logger, Path propertiesFile, Path secretPropertiesFile) {
        logger.info("Loading properties from " + propertiesFile);
        Properties properties = new Properties();
        try (FileInputStream propertiesFileStream = new FileInputStream(propertiesFile.toFile())) {
            properties.load(propertiesFileStream);
        } catch (IOException e) {
            throw new IllegalStateException(format("Could not find application properties at '%s'", propertiesFile), e);
        }
        File secretOverrides = secretPropertiesFile.toFile();
        if (secretOverrides.exists()) {
            try (FileInputStream propertiesFileStream = new FileInputStream(secretOverrides)) {
                properties.load(propertiesFileStream);
            } catch (IOException e) {
                throw new IllegalStateException(format("Problem loading '%s'", secretOverrides), e);
            }
            logger.info(format("Secret properties file %s exists. Properties have been overridden.", secretPropertiesFile));
        } else {
            logger.info(format("Secret properties file %s does not exist. No overrides made.", secretPropertiesFile));
        }
        return new Settings(properties, logger);
    }

}
