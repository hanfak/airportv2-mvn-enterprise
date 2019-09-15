package com.hanfak.airport.infrastructure.webserver;

import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RenderedContentTest {

  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final PrintWriter printWriter = mock(PrintWriter.class);

  @Test
  public void addMultipleHeadersToResponse() throws IOException {
    when(response.getWriter()).thenReturn(printWriter);
    Map<String, String> headers = new HashMap<>();
    headers.put("traceyId", "1234-1234-1234");
    headers.put("Retriable", "true");
    RenderedContent renderedContent = new RenderedContent("Blah", "text/plain", 200, headers);

    renderedContent.render(response);

    verify(response).setStatus(200);
    verify(response).setContentType("text/plain");
    verify(response).getWriter();
    verify(printWriter).print("Blah");
    verify(response).addHeader("traceyId", "1234-1234-1234");
    verify(response).addHeader("Retriable", "true");
  }
}