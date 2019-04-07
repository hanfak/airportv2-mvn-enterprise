package learning.webserver.example;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;

public class JettyServletBuilder {

    private final ServletContextHandler servletHandler;
    private final JettyWebServer webServer;

    public JettyServletBuilder(ServletContextHandler servletHandler, JettyWebServer webServer) {
        this.servletHandler = servletHandler;
        this.webServer = webServer;
    }

    public JettyWebServer build() {
        addLoggingFilter();
        return webServer.withContext(servletHandler);
    }

    public JettyServletBuilder registerEndPoint(EndPoint endPoint, TestServlet testServlet) {
        addServlet(testServlet, endPoint);
        return this;
    }

    private void addServlet(HttpServlet httpServlet, EndPoint endPoint) {
        servletHandler.addServlet(new ServletHolder(httpServlet), endPoint.path);
    }

    private void addLoggingFilter() {
        Logbook logbook = Logbook.builder()
                .writer(new DefaultHttpLogWriter(getLogger(JettyServletBuilder.class), INFO))
                .build();
        FilterHolder filterHolder = new FilterHolder(new LogbookFilter(logbook));
        servletHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST, ASYNC, ERROR));
    }
}
