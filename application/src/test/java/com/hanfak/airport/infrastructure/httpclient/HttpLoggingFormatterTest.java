package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import org.junit.Test;
import testinfrastructure.stubs.RequestStub;
import testinfrastructure.stubs.RequestWithEmptyBodyStub;
import testinfrastructure.stubs.RequestWithNoBodyStub;
import testinfrastructure.stubs.RequestWithNoHeadersStub;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("FieldCanBeLocal") // For test readability
public class HttpLoggingFormatterTest {

  @Test
  public void requestIsFormattedForLogging() throws IOException {
    String requestOutput = httpLoggingFormatter.requestOutput(request);
    assertThat(requestOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void requestWithEmptyBodyIsFormattedForLogging() throws IOException {
    String requestOutput = httpLoggingFormatter.requestOutput(requestWithEmptyBody);
    assertThat(requestOutput).isEqualTo(expectedRequestWithEmptyBodyOutput);
  }

  @Test
  public void requestWithNoBodyIsFormattedForLogging() throws IOException {
    String requestOutput = httpLoggingFormatter.requestOutput(requestWithNoBody);
    assertThat(requestOutput).isEqualTo(expectedRequestWithNoBodyOutput);
  }

  @Test
  public void requestWithNoHeadersIsFormattedForLogging() throws IOException {
    String requestOutput = httpLoggingFormatter.requestOutput(requestWithNoHeaders);
    assertThat(requestOutput).isEqualTo(expectedRequestWithNoHeadersOutput);
  }

  @Test
  public void responseIsFormattedForLogging() {
    Headers headers = new Headers();
    headers.put("key1", Arrays.asList("value1", "value2"));
    headers.put("key2", Collections.singletonList("value1"));
    when(response.getBody()).thenReturn("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"LANDED\",\n" +
            "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
            "}");
    when(response.getStatus()).thenReturn(200);
    when(response.getHeaders()).thenReturn(headers);

    String responseOutput = httpLoggingFormatter.responseOutput(response);

    assertThat(responseOutput).isEqualTo(expectedResponseOutput);
  }

  @Test
  public void responseWithNoBodyIsFormattedForLogging() {
    Headers headers = new Headers();
    headers.put("key1", Arrays.asList("value1", "value2"));
    headers.put("key2", Collections.singletonList("value1"));
    when(response.getBody()).thenReturn(null);
    when(response.getStatus()).thenReturn(200);
    when(response.getHeaders()).thenReturn(headers);

    String responseOutput = httpLoggingFormatter.responseOutput(response);

    assertThat(responseOutput).isEqualTo(expectedResponseWithNoBodyOutput);
  }

  @Test
  public void responseWithNoHeadersIsFormattedForLogging() {
    Headers headers = new Headers();
    when(response.getBody()).thenReturn(null);
    when(response.getStatus()).thenReturn(200);
    when(response.getHeaders()).thenReturn(headers);

    String responseOutput = httpLoggingFormatter.responseOutput(response);

    assertThat(responseOutput).isEqualTo(expectedResponseWithNoHeadersOutput);
  }

  private final String expectedRequestWithNoBodyOutput = "POST Some-url HTTP/1.1\n" +
          "key1: value1,value2\n" +
          "key2: value1" +
          "\r\n\r\n";
  private final String expectedResponseOutput = "HTTP 200\n" +
          "key1: value1,value2\n" +
          "key2: value1" +
          "\n\n" +
          "{\n" +
          "  \"PlaneId\" : \"A199234\",\n" +
          "  \"PlaneStatus\" : \"LANDED\",\n" +
          "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
          "}";
  private String expectedOutput = "POST Some-url HTTP/1.1\n" +
          "key1: value1,value2\n" +
          "key2: value1" +
          "\r\n\r\n" +
          "{\n" +
          "  \"PlaneId\" : \"A199234\",\n" +
          "  \"PlaneStatus\" : \"LANDED\",\n" +
          "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
          "}";
  private String expectedRequestWithEmptyBodyOutput = "POST Some-url HTTP/1.1\n" +
          "key1: value1,value2\n" +
          "key2: value1" +
          "\r\n\r\n";
  private final String expectedRequestWithNoHeadersOutput = "POST Some-url HTTP/1.1\n\r\n\r\n";
  private String expectedResponseWithNoBodyOutput = "HTTP 200\n" +
          "key1: value1,value2\n" +
          "key2: value1" +
          "\n\n";
  private final String expectedResponseWithNoHeadersOutput = "HTTP 200\n\n\n";

  private final RequestWithNoBodyStub requestWithNoBody = new RequestWithNoBodyStub(HttpMethod.POST, "Some-url");
  private final RequestWithEmptyBodyStub requestWithEmptyBody = new RequestWithEmptyBodyStub(HttpMethod.POST, "Some-url");
  private final RequestWithNoHeadersStub requestWithNoHeaders = new RequestWithNoHeadersStub(HttpMethod.POST, "Some-url");
  private final RequestStub request = new RequestStub(HttpMethod.POST, "Some-url");
  private final HttpResponse response = mock(HttpResponse.class);
  private final HttpLoggingFormatter httpLoggingFormatter = new HttpLoggingFormatter();
}