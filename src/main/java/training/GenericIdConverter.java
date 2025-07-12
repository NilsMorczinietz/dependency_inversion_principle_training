package training;/*
 * Copyright ArchiLab 2025, Cologne University of Applied Sciences, Faculty 10
 */

import jakarta.persistence.AttributeConverter;

import java.util.UUID;
import java.util.function.Function;


public abstract class GenericIdConverter<T extends GenericId> implements AttributeConverter<T, UUID> {
    private final Function<UUID, T> factory;

    protected GenericIdConverter( Function<UUID, T> factory ) {
        this.factory = factory;
    }

    @Override
    public UUID convertToDatabaseColumn( T attribute ) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public T convertToEntityAttribute( UUID dbData ) {
        return dbData == null ? null : factory.apply( dbData );
    }
}
