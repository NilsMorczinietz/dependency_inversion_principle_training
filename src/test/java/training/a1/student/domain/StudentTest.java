package training.a1.student.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void shouldCreateStudentWithValidName() {
        // given
        String studentName = "John Doe";

        // when
        Student student = new Student(studentName);

        // then
        assertNotNull(student.getId());
        assertEquals(studentName, student.getName());
    }

    @Test
    void shouldNotCreateStudentWithNullName() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Student(null));
    }

    @Test
    void shouldNotCreateStudentWithEmptyName() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Student(""));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        Student student1 = new Student("Alice Smith");
        Student student2 = new Student("Bob Jones");

        // then
        assertEquals(student1, student1); // reflexive
        assertNotEquals(student1, student2);
        assertEquals(student1.hashCode(), student1.hashCode());
    }

    @Test
    void shouldNotBeEqualToNull() {
        // given
        Student student = new Student("Test Student");

        // then
        assertNotEquals(student, null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        // given
        Student student = new Student("Test Student");
        String notAStudent = "not a student";

        // then
        assertNotEquals(student, notAStudent);
    }

    @Test
    void shouldAllowNameChanges() {
        // given
        Student student = new Student("Original Name");
        String newName = "Updated Name";

        // when
        student.setName(newName);

        // then
        assertEquals(newName, student.getName());
    }
}
