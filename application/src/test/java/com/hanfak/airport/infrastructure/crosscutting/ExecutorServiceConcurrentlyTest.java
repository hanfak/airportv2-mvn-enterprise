package com.hanfak.airport.infrastructure.crosscutting;

import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.hanfak.airport.domain.monitoring.ProbeResult.failure;
import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ExecutorServiceConcurrentlyTest {

  @Test
  public void invokingAllTasksInExecutorThrowsIllegalStateException() throws InterruptedException {
    InterruptedException cause = new InterruptedException("Blah");
    when(executorService.invokeAll(probes)).thenThrow(cause);

    ExecutorServiceConcurrently executorServiceConcurrently = new ExecutorServiceConcurrently(executorServiceFactory, 2);

    assertThatThrownBy(() -> executorServiceConcurrently.execute(probes))
            .hasMessage("java.lang.InterruptedException: Blah")
            .isInstanceOf(IllegalStateException.class)
            .hasCause(cause);
  }

  @Test
  public void shouldThrowIllegalStateExceptionWithCauseInterruptedExceptionWhenGettingAllFutures() throws InterruptedException, ExecutionException {
    InterruptedException cause = new InterruptedException("Blah");
    when(executorService.invokeAll(probes)).thenReturn(probeFutures);
    when(probeOneFuture.get()).thenReturn(success);
    when(probeTwoFuture.get()).thenThrow(cause);

    ExecutorServiceConcurrently executorServiceConcurrently = new ExecutorServiceConcurrently(executorServiceFactory, 2);

    assertThatThrownBy(() -> executorServiceConcurrently.execute(probes))
            .hasMessage("java.lang.InterruptedException: Blah")
            .isInstanceOf(IllegalStateException.class)
            .hasCause(cause);
  }

  @Test
  public void shouldThrowIllegalStateExceptionWithCauseExecutionExceptionWhenGettingAllFutures() throws InterruptedException, ExecutionException {
    RuntimeException cause = new RuntimeException("Blah");
    ExecutionException cause1 = new ExecutionException(cause);

    when(executorService.invokeAll(probes)).thenReturn(probeFutures);
    when(probeOneFuture.get()).thenReturn(success);
    when(probeTwoFuture.get()).thenThrow(cause1);

    ExecutorServiceConcurrently executorServiceConcurrently = new ExecutorServiceConcurrently(executorServiceFactory, 2);

    assertThatThrownBy(() -> executorServiceConcurrently.execute(probes))
            .hasMessage("java.util.concurrent.ExecutionException: java.lang.RuntimeException: Blah")
            .isInstanceOf(IllegalStateException.class)
            .hasCause(cause1);
  }

  @Before
  public void setUp() {
    when(probeOne.call()).thenReturn(success);
    when(probeTwo.call()).thenReturn(failure);
    when(executorServiceFactory.newFixedThreadPool(2)).thenReturn(executorService);
  }

  private final TrackingExecutorServiceFactory executorServiceFactory = mock(TrackingExecutorServiceFactory.class);
  private final ExecutorService executorService = mock(ExecutorService.class);
  private final HealthCheckProbe probeOne = mock(HealthCheckProbe.class);
  private final HealthCheckProbe probeTwo = mock(HealthCheckProbe.class);
  private final ProbeResult success = success("name", "description");
  private final ProbeResult failure = failure("name", "description");
  private final List<HealthCheckProbe> probes = Arrays.asList(probeOne, probeTwo);
  private final Future probeOneFuture = mock(Future.class);
  private final Future probeTwoFuture = mock(Future.class);
  private final List probeFutures = Arrays.asList(probeOneFuture, probeTwoFuture);
}