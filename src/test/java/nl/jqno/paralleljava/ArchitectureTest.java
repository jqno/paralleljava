package nl.jqno.paralleljava;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.SparkServer;
import nl.jqno.paralleljava.app.server.SparkServerTest;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest extends Test {

    public void architecture() {
        var importedClasses = new ClassFileImporter()
                .importPackages("nl.jqno.paralleljava");

        test("only SparkServer and SparkServerTest access Spark classes", () -> {
            var rule = noClasses()
                    .that().dontHaveFullyQualifiedName(SparkServer.class.getCanonicalName())
                    .and().dontHaveFullyQualifiedName(SparkServerTest.class.getCanonicalName())
                    .should().accessClassesThat().resideInAPackage("spark..");
            rule.check(importedClasses);
        });

        test("only Slf4jLogger accesses Slf4j classes", () -> {
            var rule = noClasses()
                    .that().resideOutsideOfPackage(Slf4jLogger.class.getPackageName())
                    .and().dontHaveFullyQualifiedName(WiredApplication.class.getCanonicalName())
                    .should().accessClassesThat().resideInAPackage("org.slf4j..");
            rule.check(importedClasses);
        });
    }
}
