package training;/*
 * Copyright ArchiLab 2025, Cologne University of Applied Sciences, Faculty 10
 */

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public abstract class GenericId implements Serializable {
    @Column(nullable = false, updatable = false)
    private final UUID id;

    protected GenericId() {
        this( UUID.randomUUID() );
    }

    protected GenericId( UUID id ) {
        this.id = id;
    }

    @Override
    public String toString(){
        return this.id.toString();
    }
}
