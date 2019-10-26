package com.hanfak.airport.infrastructure.crosscutting;

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

  public Optional<IOException> checkForInvalidJson(final String json) {
    try {
      mapper.enable(FAIL_ON_TRAILING_TOKENS);
      mapper.enable(FAIL_ON_READING_DUP_TREE_KEY);
      mapper.readTree(json);
      return empty();
    } catch (IOException e) {
      return Optional.of(e);
    }
  }
}
