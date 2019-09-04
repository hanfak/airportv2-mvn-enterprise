package com.hanfak.airport.infrastructure.webserver;

import com.google.common.annotations.VisibleForTesting;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;

import static java.lang.String.format;

public class JettyWebServer  {

    private Server server;
    private final Logger logger;

    public JettyWebServer(int port, Logger logger) {
        this.server = new Server(port);
        this.logger = logger;
    }

    @VisibleForTesting
    public JettyWebServer(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public void startServer() {
        try {
            server.start();
            logger.info("Server started at: " + server.getURI().toString());
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            String message = format("Could not start server on port '%d'", server.getURI().getPort());
            logger.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    public void stopServer() {
        try {
            server.stop();
            logger.info("Server stopped");
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            String message = format("Could not stop server on port '%d'", server.getURI().getPort());
            logger.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }
    // Test only, can remove later
    public JettyWebServer withContext(ServletContextHandler servletHandler) {
        server.setHandler(servletHandler);
        return this;
    }

    public JettyWebServer withBean(ErrorHandler errorHandler) {
        server.addBean(errorHandler);
        return this;
    }

    JettyWebServer withHandler(Handler handler) {
        server.setHandler(handler);
        return this;
    }
}
