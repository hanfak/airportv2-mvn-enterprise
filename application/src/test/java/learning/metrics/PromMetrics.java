package learning.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PromMetrics {
  static class ExampleServlet extends HttpServlet {
    static final Counter requests = Counter.build()
            .name("hello_worlds_total")
            .help("Number of hello worlds served.").register();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
      resp.getWriter().println("Hello World!");
      // Increment the number of requests.
      requests.inc();
    }
  }

  public static void main( String[] args ) throws Exception {
    Server server = new Server(12345);
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    server.setHandler(context);
    // Expose our example servlet.
    context.addServlet(new ServletHolder(new ExampleServlet()), "/");
    // Expose Promtheus metrics.
    context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
    // Add metrics about CPU, JVM memory etc.


    DefaultExports.initialize();


    // Start the webserver.
    server.start();
    server.join();



  }
}
