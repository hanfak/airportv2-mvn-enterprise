package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;

public class JettyServletBuilder {

    private final ServletContextHandler servletContextHandler ;
    private final JettyWebServer webServer;

    public JettyServletBuilder(ServletContextHandler servletContextHandler, JettyWebServer webServer) {
        this.servletContextHandler = servletContextHandler;
        this.webServer = webServer;
    }

    public JettyWebServer build() {
        return webServer.withContext(servletContextHandler);
    }

    public JettyServletBuilder registerEndPoint(EndPoint endPoint, LandAirplaneServlet servlet) {
        addServlet(servlet, endPoint);
        return this;
    }

    private void addServlet(HttpServlet httpServlet, EndPoint endPoint) {
        servletContextHandler.addServlet(new ServletHolder(httpServlet), endPoint.path);
    }
}
