package learning.webserver.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import static learning.webserver.example.EndPoint.get;

public class WebServerRunner {
    private JettyWebServer webServer;

    public static void main(String[] args) {
        WebServerRunner webServerRunner = new WebServerRunner();
        webServerRunner.startWebServer();
    }

    public void startWebServer() {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        JettyWebServer server = new JettyWebServer(new Server(1234));
        webServer = new JettyServletBuilder(servletContextHandler, server)
                .registerEndPoint(get("/test"), new TestServlet())
                .build();
        webServer.startServer();
    }

    public void stop() {
        webServer.stopServer();
    }
}
