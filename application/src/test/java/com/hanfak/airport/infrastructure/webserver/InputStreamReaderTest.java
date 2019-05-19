package com.hanfak.airport.infrastructure.webserver;

import org.apache.commons.io.input.NullInputStream;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InputStreamReaderTest implements WithAssertions {
  @Test
  public void readsEmptyInputStream() {
    String string = InputStreamReader.readInputStream(new NullInputStream(0));
    assertThat(string).isEmpty();
  }

  @Test
  public void readsNonEmptyInputStream() {
    String string = InputStreamReader.readInputStream(new ByteArrayInputStream("hello world".getBytes(UTF_8)));
    assertThat(string).isEqualTo("hello world");
  }

  @Test
  public void forCodeCoverage() {
    InputStreamReader inputStreamReader = new InputStreamReader();

    assertThat(inputStreamReader).isInstanceOf(InputStreamReader.class);
  }
}