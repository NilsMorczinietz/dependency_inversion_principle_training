package training.a1.course.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CourseIdConverterTest {

    private CourseIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CourseIdConverter();
    }

    @Test
    void shouldConvertToDatabaseColumn() {
        // given
        UUID uuid = UUID.randomUUID();
        CourseId courseId = new CourseId(uuid);

        // when
        UUID result = converter.convertToDatabaseColumn(courseId);

        // then
        assertEquals(uuid, result);
    }

    @Test
    void shouldConvertNullToDatabaseColumn() {
        // when
        UUID result = converter.convertToDatabaseColumn(null);

        // then
        assertNull(result);
    }

    @Test
    void shouldConvertToEntityAttribute() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        CourseId result = converter.convertToEntityAttribute(uuid);

        // then
        assertNotNull(result);
        assertEquals(uuid, result.getId());
    }

    @Test
    void shouldConvertNullToEntityAttribute() {
        // when
        CourseId result = converter.convertToEntityAttribute(null);

        // then
        assertNull(result);
    }

    @Test
    void shouldRoundTripConversion() {
        // given
        CourseId originalCourseId = new CourseId();

        // when
        UUID databaseValue = converter.convertToDatabaseColumn(originalCourseId);
        CourseId convertedBack = converter.convertToEntityAttribute(databaseValue);

        // then
        assertEquals(originalCourseId, convertedBack);
    }
}
