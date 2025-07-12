package training.a1.student.domain;

import jakarta.persistence.Converter;
import training.GenericIdConverter;

@Converter(autoApply = true)
public class StudentIdConverter extends GenericIdConverter<StudentId> {
    public StudentIdConverter() {
        super( StudentId::new );
    }
}
