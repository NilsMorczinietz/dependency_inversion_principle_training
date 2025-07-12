package training.a1.course.domain;

import jakarta.persistence.Converter;
import training.GenericIdConverter;

@Converter(autoApply = true)
public class CourseIdConverter extends GenericIdConverter<CourseId> {
    public CourseIdConverter() {
        super( CourseId::new );
    }
}
