package com.hanfak.airport.infrastructure.httpclient;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimerFactoryTest {
  @Test
  public void startsTimer() {
    assertThat(new TimerFactory().startTimer()).isInstanceOf(Timer.class);
  }
}