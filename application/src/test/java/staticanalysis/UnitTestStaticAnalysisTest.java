package staticanalysis;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static testinfrastructure.FileHelpers.listDirectory;

public class UnitTestStaticAnalysisTest implements WithAssertions {

    @Test
    public void checkThatUnitTestsAreInTheSamePackageAsTheProductionClasses() throws IOException {
        List<Path> violations = listDirectory(Paths.get("src/test/java/com/hanfak/airport"))
                .stream()
                .filter(this::isJavaFile)
                .filter(this::productionFileIsMissing)
                .collect(toList());

        assertThat(violations).describedAs("The following unit tests were not in the same package as the production code").isEmpty();
    }

    private boolean isJavaFile(Path file) {
        return file.toString().endsWith("java");
    }

    private boolean productionFileIsMissing(Path unitTest) {
        String productionFileName = unitTest.getFileName().toString().replace("Test", "");
        int pathLength = unitTest.getNameCount();
        Path commonPath = unitTest.subpath(2, pathLength - 1);
        Path productionFile = Paths.get("src/main").resolve(commonPath).resolve(productionFileName);
        return !productionFile.toFile().exists();
    }
}