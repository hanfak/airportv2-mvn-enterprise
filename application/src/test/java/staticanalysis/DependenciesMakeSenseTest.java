package staticanalysis;

import io.github.theangrydev.domainenforcer.DomainEnforcer;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.github.theangrydev.domainenforcer.DomainEnforcer.enforceSources;
public class DependenciesMakeSenseTest implements WithAssertions {

  @Test
  public void domainShouldOnlyTalkToItselfAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.domain")
            .apartFrom("java", LOGGER, APACHE_COMMONS_BUILDERS, FINDBUGS);

    assertThat(violations).describedAs("Violations").isEmpty();
  }

  @Test
  public void applicationShouldOnlyTalkToItselfAndDomainAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.usecase")
            .apartFrom("com.hanfak.airport.domain", LOGGER, "java");

    assertThat(violations).describedAs("Violations").isEmpty();
  }

  //TODO fix so can talk to everything except for wiring
  @Test
  public void infrastructureShouldOnlyTalkToItselfAndUsecaseAndDomainAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.infrastructure")
            .apartFrom("com.hanfak.airport.domain",
                    "com.hanfak.airport.usecase",
                    LOGGER,
                    HIKARI,
                    APACHE_COMMONS_STRING_UTILS,
                    GUAVA,
                    "com.google.common.util.concurrent.ThreadFactoryBuilder",
                    "com.google.common.base.Suppliers",
                    "org.eclipse.jetty",
                    "org.json",
                    "com.fasterxml.jackson",
                    "org.zalando.logbook",
                    "io.prometheus.client",
                    "com.mashape.unirest.http",
                    "com.mashape.unirest.request",
                    FINDBUGS,
                    "java");
    assertThat(violations).describedAs("Violations").isEmpty();
  }
  // TODO: This is not needed as wiring should talk to everything
  @Test
  public void wiringShouldOnlyTalkToItselfAndInfrastructureAndUsecaseAndDomainAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.wiring")
            .apartFrom("com.hanfak.airport.domain",
                    "com.hanfak.airport.usecase",
                    "com.hanfak.airport.infrastructure",
                    "org.eclipse.jetty.servlet",
                    "com.google.common.annotations.VisibleForTesting",
                    "com.fasterxml.jackson",
                    "io.prometheus.client",
                    "org.eclipse.jetty.server",
                    LOGGER, "java");

    assertThat(violations).describedAs("Violations").isEmpty();
  }

  private final DomainEnforcer domainEnforcer = enforceSources(Paths.get("./src/main/java"));
  private static final String APACHE_COMMONS_BUILDERS = "org.apache.commons.lang3.builder";
  private static final String APACHE_COMMONS_STRING_UTILS = "org.apache.commons.lang3.StringUtils";
  private static final String LOGGER = "org.slf4j.Logger";
  private static final String FINDBUGS = "edu.umd.cs.findbugs.annotations.SuppressFBWarnings";
  private static final String HIKARI = "com.zaxxer.hikari";
  private static final String GUAVA = "com.google.common";
}
