package training.a1.student.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudentIdTest {

    @Test
    void shouldCreateStudentIdWithRandomUUID() {
        // when
        StudentId studentId = new StudentId();

        // then
        assertNotNull(studentId);
        assertNotNull(studentId.getId());
    }

    @Test
    void shouldCreateStudentIdWithSpecificUUID() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        StudentId studentId = new StudentId(uuid);

        // then
        assertNotNull(studentId);
        assertEquals(uuid, studentId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        UUID uuid = UUID.randomUUID();
        StudentId studentId1 = new StudentId(uuid);
        StudentId studentId2 = new StudentId(uuid);
        StudentId studentId3 = new StudentId(UUID.randomUUID());

        // then
        assertEquals(studentId1, studentId2);
        assertEquals(studentId1.hashCode(), studentId2.hashCode());
        assertNotEquals(studentId1, studentId3);
        assertNotEquals(studentId1.hashCode(), studentId3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // given
        StudentId studentId = new StudentId();

        // when
        String toString = studentId.toString();

        // then
        assertNotNull(toString);
        assertTrue(toString.contains(studentId.getId().toString()));
    }

    @Test
    void shouldNotBeEqualToNull() {
        // given
        StudentId studentId = new StudentId();

        // then
        assertNotEquals(studentId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        // given
        StudentId studentId = new StudentId();
        String notAStudentId = "not a student id";

        // then
        assertNotEquals(studentId, notAStudentId);
    }
}
