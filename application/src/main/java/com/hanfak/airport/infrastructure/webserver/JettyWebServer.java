package com.hanfak.airport.infrastructure.webserver;

import com.google.common.annotations.VisibleForTesting;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import static java.lang.String.format;

public class JettyWebServer  {

    private Server server;

    public JettyWebServer(int port) {
        this.server = new Server(port);
    }

    @VisibleForTesting
    public JettyWebServer(Server server) {
        this.server = server;
    }

    public void startServer() {
        try {
            server.start();
            //  System.out.println(server.getURI().toString()); // TODO add logger, instead of sout, and test
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            throw new IllegalStateException(format("Could not startServer server on port '%d'", server.getURI().getPort()), e);
        }
    }

    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            throw new IllegalStateException(format("Could not stop server on port '%d'", server.getURI().getPort()), e);
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
