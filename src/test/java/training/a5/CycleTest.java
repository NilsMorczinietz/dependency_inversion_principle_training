package training.a5;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class CycleTest {
    private JavaClasses importedClasses = new ClassFileImporter().importPackages("training.a5");

    @Test
    public void testNoCyclicDependenciesBetweenPackages() {
        ArchRule rule = slices().matching("..a5.(*)..").should().beFreeOfCycles();
        rule.check(importedClasses);
    }
}
