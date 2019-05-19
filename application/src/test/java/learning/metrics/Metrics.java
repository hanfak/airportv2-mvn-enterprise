package learning.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.jvm.CachedThreadStatesGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Metrics {

  public static void main(String... args) {
    MetricRegistry registry = new MetricRegistry();
    registry.register("gc", new GarbageCollectorMetricSet());
    registry.register("threads", new CachedThreadStatesGaugeSet(10, TimeUnit.SECONDS));
    registry.register("memory", new MemoryUsageGaugeSet());

    final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
            .outputTo(LoggerFactory.getLogger("com.example.metrics"))
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
    reporter.start(1, TimeUnit.MINUTES);
    Meter requests = registry.meter("requests");
    requests.mark();
  }


//  public static MetricRegistry registry() {
//    return registry;
//  }
//
//  public static Timer timer(String first, String... keys) {
//    return registry.timer(MetricRegistry.name(first, keys));
//  }
//
//  public static Meter meter(String first, String... keys) {
//    return registry.meter(MetricRegistry.name(first, keys));
//  }
//
//  static String metricPrefix(String app) {
//    Env env = Env.get();
//    String host = env == Env.LOCAL ? "localhost" : getHost();
//    String prefix = MetricRegistry.name(app, env.getName(), host);
//    log.info("Setting Metrics Prefix {}", prefix);
//    return prefix;
//  }
//
//  private static String getHost() {
//    return EC2MetadataUtils.getLocalHostName().split("\\.")[0];
//  }
}
