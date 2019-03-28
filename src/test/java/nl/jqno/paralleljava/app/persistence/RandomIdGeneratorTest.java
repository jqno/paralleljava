package nl.jqno.paralleljava.app.persistence;

import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.picotest.Test;
import org.assertj.core.api.Assertions;

import java.util.UUID;

public class RandomIdGeneratorTest extends Test {

    public void uuidGenerator() {
        var generator = Wiring.randomIdGenerator();

        test("generates a valid uuid", () -> {
            var actual = generator.generateId();
            var roundTrip = UUID.fromString(actual.toString());
            Assertions.assertThat(actual).isEqualTo(roundTrip);
        });
    }
}
