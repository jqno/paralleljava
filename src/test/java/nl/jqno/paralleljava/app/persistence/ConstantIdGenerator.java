package nl.jqno.paralleljava.app.persistence;

import nl.jqno.paralleljava.app.persistence.IdGenerator;

import java.util.UUID;

public class ConstantIdGenerator implements IdGenerator {
    private final UUID id;

    public ConstantIdGenerator(UUID id) {
        this.id = id;
    }

    public UUID generateId() {
        return id;
    }
}
