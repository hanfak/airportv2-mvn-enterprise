package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.ready.ReadyServlet;
import com.hanfak.airport.infrastructure.webserver.notfound.NotFoundServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;

import javax.servlet.http.HttpServlet;

public class JettyServletBuilder {

    private final ServletContextHandler servletContextHandler ;
    private final JettyWebServer webServer;
    private final Logger logger;

    public JettyServletBuilder(ServletContextHandler servletContextHandler, JettyWebServer webServer, Logger logger) {
        this.servletContextHandler = servletContextHandler;
        this.webServer = webServer;
        this.logger = logger;
    }

    public JettyWebServer build() {
        addServlet(new NotFoundServlet(), "/");
        addErrorHandler(new UncaughtErrorHandler(logger));
        return webServer
                .withContext(servletContextHandler);
    }

    public JettyServletBuilder registerEndPoint(EndPoint endPoint, LandAirplaneServlet servlet) {
        addServlet(servlet, endPoint);
        return this;
    }

    public JettyServletBuilder registerReadyPageEndPoint(EndPoint endPoint, ReadyServlet servlet) {
        addServlet(servlet, endPoint);
        return this;
    }

    private void addServlet(HttpServlet httpServlet, EndPoint endPoint) {
        addServlet(httpServlet, endPoint.path);
    }

    private void addServlet(HttpServlet httpServlet, String path) {
        servletContextHandler.addServlet(new ServletHolder(httpServlet), path);
    }

    private void addErrorHandler(UncaughtErrorHandler uncaughtErrorHandler) {
        webServer.withBean(uncaughtErrorHandler);
    }
}
