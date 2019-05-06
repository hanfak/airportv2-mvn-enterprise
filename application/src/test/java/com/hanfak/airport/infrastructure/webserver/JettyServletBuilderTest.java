package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import org.assertj.core.api.WithAssertions;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyServletBuilderTest implements WithAssertions {

    private final ServletContextHandler servletHandler = mock(ServletContextHandler.class);
    private final JettyWebServer webServer = mock(JettyWebServer.class);
    private final LandAirplaneServlet servlet = mock(LandAirplaneServlet.class);

    private final JettyServletBuilder JettyServletBuilder = new JettyServletBuilder(servletHandler, webServer, mock(Logger.class));
    
    @Test
    public void shouldAddServletHandlerToServerWhenBuilt() {
        ArgumentCaptor<UncaughtErrorHandler> parameterCaptor = ArgumentCaptor
                .forClass(UncaughtErrorHandler.class);

        when(webServer.withContext(any())).thenReturn(webServer);
        JettyServletBuilder.registerEndPoint(EndPoint.get("/path"), servlet);
        JettyServletBuilder.build();

        verify(webServer).withContext(servletHandler);
        verify(webServer).withBean(parameterCaptor.capture());
    }
    
    @Test
    public void shouldAddServletToHandler() {
        JettyServletBuilder.registerEndPoint(EndPoint.post("/path"), servlet);

        verify(servletHandler).addServlet(any(ServletHolder.class), anyString());
    }
}

