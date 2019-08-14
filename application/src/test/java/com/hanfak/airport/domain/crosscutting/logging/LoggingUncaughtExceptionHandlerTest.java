package com.hanfak.airport.domain.crosscutting.logging;

import org.junit.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class LoggingUncaughtExceptionHandlerTest {

  private final Logger logger = mock(Logger.class);

  @Test
  public void uncaughtExceptionInThreadIsLogged() {
    Thread thread = new Thread("DummyThread");
    Throwable throwable = new UnsupportedOperationException();

    LoggingUncaughtExceptionHandler loggingUncaughtExceptionHandler = new LoggingUncaughtExceptionHandler(logger);
    loggingUncaughtExceptionHandler.uncaughtException(thread, throwable);

    verify(logger).error("Uncaught exception in thread 'DummyThread'", throwable);
  }
}