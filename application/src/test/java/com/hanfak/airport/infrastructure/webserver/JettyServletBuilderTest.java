package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.LandAirplaneServlet;
import org.assertj.core.api.WithAssertions;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JettyServletBuilderTest implements WithAssertions {

    private ServletContextHandler servletHandler = mock(ServletContextHandler.class);
    private JettyWebServer webServer = mock(JettyWebServer.class);
    private LandAirplaneServlet servlet = mock(LandAirplaneServlet.class);

    private final JettyServletBuilder JettyServletBuilder = new JettyServletBuilder(servletHandler, webServer);
    
    @Test
    public void shouldAddServletHandlerToServerWhenBuilt() {
        JettyServletBuilder.registerEndPoint(EndPoint.get("/path"), servlet);
        JettyServletBuilder.build();

        verify(webServer).withContext(servletHandler);
    }
    
    @Test
    public void shouldAddServletToHandler() {
        JettyServletBuilder.registerEndPoint(EndPoint.get("/path"), servlet);

        verify(servletHandler).addServlet(any(ServletHolder.class), anyString());
    }
}

