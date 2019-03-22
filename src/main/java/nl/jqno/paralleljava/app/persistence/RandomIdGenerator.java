package nl.jqno.paralleljava.app.persistence;

import java.util.UUID;

public class RandomIdGenerator implements IdGenerator {
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
