package training.a1.student.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudentIdConverterTest {

    private StudentIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StudentIdConverter();
    }

    @Test
    void shouldConvertToDatabaseColumn() {
        // given
        UUID uuid = UUID.randomUUID();
        StudentId studentId = new StudentId(uuid);

        // when
        UUID result = converter.convertToDatabaseColumn(studentId);

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
        StudentId result = converter.convertToEntityAttribute(uuid);

        // then
        assertNotNull(result);
        assertEquals(uuid, result.getId());
    }

    @Test
    void shouldConvertNullToEntityAttribute() {
        // when
        StudentId result = converter.convertToEntityAttribute(null);

        // then
        assertNull(result);
    }

    @Test
    void shouldRoundTripConversion() {
        // given
        StudentId originalStudentId = new StudentId();

        // when
        UUID databaseValue = converter.convertToDatabaseColumn(originalStudentId);
        StudentId convertedBack = converter.convertToEntityAttribute(databaseValue);

        // then
        assertEquals(originalStudentId, convertedBack);
    }
}
