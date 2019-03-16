package staticanalysis;

import io.github.theangrydev.domainenforcer.DomainEnforcer;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static io.github.theangrydev.domainenforcer.DomainEnforcer.enforceSources;

public class DependenciesMakeSenseTest implements WithAssertions {

  private final DomainEnforcer domainEnforcer = enforceSources(Paths.get("./src/main/java"));
  private static final String APACHE_COMMONS_BUILDERS = "org.apache.commons.lang3.builder";
  private static final String LOGGER = "org.slf4j.Logger";
  private static final String FINDBUGS = "edu.umd.cs.findbugs.annotations.SuppressFBWarnings";

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

  @Test
  public void infrastructureShouldOnlyTalkToItselfAndUsecaseAndDomainAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.infrastructure")
            .apartFrom("com.hanfak.airport.domain", "com.hanfak.airport.usecase", LOGGER, "java");

    assertThat(violations).describedAs("Violations").isEmpty();
  }

  @Test
  public void wiringShouldOnlyTalkToItselfAndInfrastructureAndUsecaseAndDomainAndJava() {
    List<String> violations = domainEnforcer.checkThatPackageOnlyTalksToItself("com.hanfak.airport.wiring")
            .apartFrom("com.hanfak.airport.domain", "com.hanfak.airport.usecase", "com.hanfak.airport.infrastructure", LOGGER, "java");

    assertThat(violations).describedAs("Violations").isEmpty();
  }
}
