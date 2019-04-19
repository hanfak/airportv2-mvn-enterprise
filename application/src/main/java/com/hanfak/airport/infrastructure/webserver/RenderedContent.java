package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.domain.helper.ValueType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RenderedContent extends ValueType {

    public final String body;
    public final String contentType;
    public final int statusCode;

    public RenderedContent(String body, String contentType, int statusCode) {
        this.body = body;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public void render(HttpServletResponse response) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(contentType);
        response.getWriter().print(body);
    }
}
