package com.hanfak.airport.infrastructure.httpclient;

import org.junit.Test;

import static java.time.Duration.ZERO;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class TimerTest {

  @Test
  public void timesElapsedDuration() throws InterruptedException {
    Timer timer = Timer.start();

    MILLISECONDS.sleep(1);

    assertThat(timer.elapsedTime()).isGreaterThan(ZERO);
  }
}