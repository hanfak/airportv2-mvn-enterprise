package com.hanfak.airport.infrastructure.webserver;

import org.junit.Test;
import testinfrastructure.stubs.TestLogger;

import javax.servlet.http.HttpServletRequest;
import java.io.Writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UncaughtErrorHandlerTest {

  private final TestLogger testLogger = new TestLogger();
  private final UncaughtErrorHandler errorHandler = new UncaughtErrorHandler(testLogger);
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final Writer writer = mock(Writer.class);

  @Test
  public void shouldLogTheErrors() throws Exception {
    RuntimeException exception = new RuntimeException("This is an exceptional situation!!!!");
    when(request.getAttribute("javax.servlet.error.exception")).thenReturn(exception);

    errorHandler.writeErrorPage(request, writer, 500, "jibberish", false);

    assertThat(testLogger.errorLogs()).containsExactly("Uncaught exception: 'This is an exceptional situation!!!!'");
    verify(writer).append("Technical Failure. Please contact the system administrator.");
  }

  @Test
  public void shouldLogFatalErrorWhenThereIsNoExceptionInTheRequestAttributes() throws Exception {
    when(request.getAttribute("javax.servlet.error.exception")).thenReturn(null);

    errorHandler.writeErrorPage(request, writer, 500, "jibberish", false);

    assertThat(testLogger.errorLogs()).containsExactly("Fatal error. Uncaught exception was not found.");
    verify(writer).append("Technical Failure. Please contact the system administrator.");
  }
}