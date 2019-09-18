package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.domain.helper.ValueType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RenderedContent extends ValueType {

    public final String body;
    private final String contentType;
    public final int statusCode;
    public final Map<String, String> headers;

    public RenderedContent(String body, String contentType, int statusCode, Map<String, String> headers) {
        this.body = body;
        this.contentType = contentType;
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public RenderedContent(String body, String contentType, int statusCode) {
        this(body, contentType, statusCode, new HashMap<>());
    }

    public void render(HttpServletResponse response) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(contentType);
        response.getWriter().print(body);
        headers.forEach(response::addHeader);
    }
}
