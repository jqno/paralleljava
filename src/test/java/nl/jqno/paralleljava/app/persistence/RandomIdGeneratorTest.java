package nl.jqno.paralleljava.app.persistence;

import nl.jqno.picotest.Test;
import org.assertj.core.api.Assertions;

import java.util.UUID;

public class RandomIdGeneratorTest extends Test {

    public void uuidGenerator() {
        var generator = new RandomIdGenerator();

        test("generates a valid uuid", () -> {
            UUID actual = generator.generateId();
            var roundTrip = UUID.fromString(actual.toString());
            Assertions.assertThat(actual).isEqualTo(roundTrip);
        });
    }
}
