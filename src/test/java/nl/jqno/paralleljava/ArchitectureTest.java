package nl.jqno.paralleljava;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import junit.framework.TestCase;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.SparkServer;
import nl.jqno.paralleljava.server.SparkServerTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest extends TestCase {

    private static final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("nl.jqno.paralleljava");

    public void testThatOnlySparkServerAccessesSparkClasses() {
        var rule = noClasses()
                .that().dontHaveFullyQualifiedName(SparkServer.class.getCanonicalName())
                .and().dontHaveFullyQualifiedName(SparkServerTest.class.getCanonicalName())
                .should().accessClassesThat().resideInAPackage("spark..");
        rule.check(importedClasses);
    }

    public void testThatOnlySlf4jLoggerAccessesSlf4jClasses() {
        var rule = noClasses()
                .that().dontHaveFullyQualifiedName(Slf4jLogger.class.getCanonicalName())
                .should().accessClassesThat().resideInAPackage("org.slf4j..");
        rule.check(importedClasses);
    }
}
