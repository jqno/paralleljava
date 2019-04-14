package nl.jqno.paralleljava;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.persistence.database.DatabaseRepository;
import nl.jqno.paralleljava.app.serialization.GsonSerializer;
import nl.jqno.paralleljava.app.server.SparkServer;
import nl.jqno.picotest.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest extends Test {

    private static final JavaClasses IMPORTED_CLASSES =
            new ClassFileImporter().importPackages("nl.jqno.paralleljava");

    public void architecture() {
        test("only SparkServer and SparkServerTest access Spark classes", () -> {
            assertBoundary("spark..", SparkServer.class.getPackage());
        });

        test("only Slf4jLogger accesses Slf4j classes", () -> {
            assertBoundary("org.slf4j..", Slf4jLogger.class.getPackage());
        });

        test("only GsonSerializer accesses Gson classes", () -> {
            assertBoundary("com.google.gson..", GsonSerializer.class.getPackage());
        });

        test("only DatabaseRepository accesses Engine classes", () -> {
            assertBoundary("org.jdbi..", DatabaseRepository.class.getPackage());
        });
    }

    private void assertBoundary(String restrictedPackageIdentifier, Package whiteListedPackage) {
        var rule = noClasses()
                .that().resideOutsideOfPackage(whiteListedPackage.getName())
                .and().dontHaveFullyQualifiedName(Main.class.getCanonicalName())
                .should().accessClassesThat().resideInAPackage(restrictedPackageIdentifier);
        rule.check(IMPORTED_CLASSES);
    }
}
