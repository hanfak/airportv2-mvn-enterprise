package com.hanfak.airport.infrastructure.crosscutting;

import com.hanfak.airport.domain.crosscutting.logging.LoggingUncaughtExceptionHandler;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrackingExecutorServiceFactoryTest {

  private static final RuntimeException UNCAUGHT_EXCEPTION = new RuntimeException();

  private final LoggingUncaughtExceptionHandler loggingUncaughtExceptionHandler = mock(LoggingUncaughtExceptionHandler.class);
  private final TrackingExecutorServiceFactory executorServiceFactory = new TrackingExecutorServiceFactory(loggingUncaughtExceptionHandler);

  /**
   * For {@link ExecutorService#submit(Callable)}, the {@link LoggingUncaughtExceptionHandler} is not called, since
   * the exceptions are managed by the {@link Future}, e.g. {@link Future#get()} can throw an exception.
   */
  @Test
  public void uncaughtExceptionsAreLoggedForTheExecuteMethod() throws InterruptedException {
    ExecutorService executorService = executorServiceFactory.newFixedThreadPool(1);
    executorService.execute(() -> {throw UNCAUGHT_EXCEPTION;});

    executorServiceFactory.shutdown();

    Thread.sleep(30);

    verify(loggingUncaughtExceptionHandler).uncaughtException(any(), eq(UNCAUGHT_EXCEPTION));
  }

  @Test
  public void noThreadsAreLeftOverOnShutdownForFixedThreadPool() throws InterruptedException {
    ExecutorService executorService = executorServiceFactory.newFixedThreadPool(2);
    executorService.submit(this::someWork);
    executorService.submit(this::someWork);

    executorServiceFactory.shutdown();

    assertThat(executorService.isTerminated()).isTrue();
  }

  private void someWork() {
    System.out.println(Thread.currentThread().getName());
  }

}