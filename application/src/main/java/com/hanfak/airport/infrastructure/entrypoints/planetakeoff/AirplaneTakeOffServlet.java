package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.hanfak.airport.infrastructure.webserver.RenderedContent;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.hanfak.airport.infrastructure.webserver.InputStreamReader.readInputStream;

// Not unit tested as tested in end to end tests see ApplicationServerTest.java
public class AirplaneTakeOffServlet extends HttpServlet {

    private final AirplaneTakeOffWebservice webservice;

    public AirplaneTakeOffServlet(AirplaneTakeOffWebservice webservice) {
        this.webservice = webservice;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = readInputStream(request.getInputStream());
        RenderedContent httpResponse = webservice.execute(requestBody);
        httpResponse.render(response);
    }
}
