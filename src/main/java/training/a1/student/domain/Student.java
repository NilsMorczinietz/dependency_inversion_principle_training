package training.a1.student.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Student {
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private StudentId id;

    private String name;

    public Student(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name or immatriculation number invalid");
        this.name = name;
        this.id = new StudentId();
    }
}
