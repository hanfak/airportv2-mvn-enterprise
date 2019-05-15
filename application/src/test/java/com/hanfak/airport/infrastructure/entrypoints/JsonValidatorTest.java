package com.hanfak.airport.infrastructure.entrypoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_TRAILING_TOKENS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonValidatorTest {

  @Test
  public void validJson() throws Exception {
    Optional<JsonProcessingException> actualResult = jsonValidator.checkForInvalidJson(someJson);

    verify(mapper).enable(FAIL_ON_TRAILING_TOKENS);
    verify(mapper).enable(FAIL_ON_READING_DUP_TREE_KEY);
    verify(mapper).readTree(someJson);
    assertThat(actualResult).isEmpty();
  }

  @Test
  public void invalidJson() throws Exception {
    ExceptionMock exception = new ExceptionMock("blah");
    when(mapper.readTree(invalidJson)).thenThrow(exception);
    Optional<JsonProcessingException> actualResult = jsonValidator.checkForInvalidJson(invalidJson);

    assertThat(actualResult).contains(exception);
  }

  @Test
  public void ioexception() throws Exception {
    IOException exception = new IOException("blah");
    when(mapper.readTree(invalidJson)).thenThrow(exception);
    assertThatThrownBy(() -> jsonValidator.checkForInvalidJson(invalidJson))
            .isInstanceOf(RuntimeException.class)
            .hasCause(exception);
  }

  private static final String someJson = "{\"field\" : \"Some json\"}";
  private static final String invalidJson = "blah";

  private final ObjectMapper mapper = mock(ObjectMapper.class);
  private final JsonValidator jsonValidator = new JsonValidator(mapper);

  class ExceptionMock extends JsonProcessingException {
    ExceptionMock(String msg) {
      super(msg);
    }
  }
}