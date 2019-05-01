package learning.restfulserver.example1;

import learning.restfulserver.example1.app.RealClock;
import learning.restfulserver.example1.app.TestUseCase;
import learning.restfulserver.example1.resources.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

//https://self-learning-java-tutorial.blogspot.com/2016/11/embedded-jetty-jersey-example.html
public class TestServer {

  public final static String PATH = "time";

  public static void main(String... args) {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    Server jettyServer = new Server(8080);
    jettyServer.setHandler(context);

    // Using resource config - nicer
    ResourceConfig config = new ResourceConfig();
    config.register(new TestResource(new TestUseCase()));
    config.register(new TestTwoResource());
    config.register(new TestThreeResource());
    config.register(new TestFourResource());
    config.register(new TimeResource(new RealClock()));
    ServletHolder servlet = new ServletHolder(new ServletContainer(config));
    context.addServlet(servlet, "/*");


//    ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
//    jerseyServlet.setInitOrder(0);

    // For one resource
//    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", TestResource.class.getCanonicalName(), );

    // For multiple resource - using reflection, harder to pass dependencies in
//    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
//            join(",", asList(TestResource.class.getCanonicalName(),
//                    TestTwoResource.class.getCanonicalName(),
//                    TestThreeResource.class.getCanonicalName(),
//                    TestFourResource.class.getCanonicalName())));
    try {
      jettyServer.start();
      jettyServer.join();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jettyServer.destroy();
    }
  }
}

