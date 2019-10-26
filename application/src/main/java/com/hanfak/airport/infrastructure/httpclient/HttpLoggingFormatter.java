package com.hanfak.airport.infrastructure.httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class HttpLoggingFormatter {
  public String requestOutput(HttpRequest httpRequest) {
    return format("%s %s HTTP/1.1%s", httpRequest.getHttpMethod(), httpRequest.getUrl(), "\n")
            + headersFormatter(httpRequest.getHeaders())
            + "\r\n\r\n"
            + requestBody(httpRequest);
  }

  public String responseOutput(HttpResponse response) {
    return format("%s %s%n%s%n%n%s", "HTTP", response.getStatus(), headersFormatter(response.getHeaders()), responseBody(response));
  }

  private String requestBody(HttpRequest httpRequest) {
    return Optional.ofNullable( httpRequest.getBody())
            .map(Object::toString)
            .orElse("");
  }

  private String responseBody(HttpResponse httpRequest) {
    return Optional.ofNullable( httpRequest.getBody())
            .map(Object::toString)
            .orElse("");
  }

  private String headersFormatter(Map<String, List<String>> headers) {
    return headers.entrySet().stream()
            .map(header -> format("%s: %s", header.getKey(), String.join(",", header.getValue())))
            .collect(joining(lineSeparator()));
  }
}
