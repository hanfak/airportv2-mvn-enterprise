package com.hanfak.airport.infrastructure.entrypoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_TRAILING_TOKENS;
import static java.util.Optional.empty;

public class JsonValidator {

  private final ObjectMapper mapper;

  public JsonValidator(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes") // TODO fix by using custom exception
  public Optional<JsonProcessingException> checkForInvalidJson(final String json) {
    try {
      mapper.enable(FAIL_ON_TRAILING_TOKENS);
      mapper.enable(FAIL_ON_READING_DUP_TREE_KEY);
      mapper.readTree(json);
      return empty();
    } catch (JsonProcessingException e) {
      return Optional.of(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
