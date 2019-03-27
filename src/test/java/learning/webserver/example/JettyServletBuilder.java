package learning.webserver.example;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;

public class JettyServletBuilder {

    private final ServletContextHandler servletHandler;
    private final JettyWebServer webServer;

    public JettyServletBuilder(ServletContextHandler servletHandler, JettyWebServer webServer) {
        this.servletHandler = servletHandler;
        this.webServer = webServer;
    }

    public JettyWebServer build() {
        return webServer.withContext(servletHandler);
    }

    public JettyServletBuilder registerEndPoint(EndPoint endPoint, TestServlet testServlet) {
        addServlet(testServlet, endPoint);
        return this;
    }

    private void addServlet(HttpServlet httpServlet, EndPoint endPoint) {
        servletHandler.addServlet(new ServletHolder(httpServlet), endPoint.path);
    }
}
