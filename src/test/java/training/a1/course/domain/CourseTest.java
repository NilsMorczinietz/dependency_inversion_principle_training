package training.a1.course.domain;

import org.junit.jupiter.api.Test;
import training.a1.student.domain.Student;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void shouldCreateCourseWithValidData() {
        // given
        String courseName = "Software Engineering";
        Integer ects = 6;

        // when
        Course course = new Course(courseName, ects);

        // then
        assertNotNull(course.getId());
        assertEquals(courseName, course.getName());
        assertEquals(ects, course.getEcts());
        assertTrue(course.getEnrolledStudents().isEmpty());
    }

    @Test
    void shouldEnrollStudents() {
        // given
        Course course = new Course("Java Programming", 4);
        Student student1 = new Student("John Doe");
        Student student2 = new Student("Jane Smith");

        // when
        course.enrollStudent(student1);
        course.enrollStudent(student2);

        // then
        assertEquals(2, course.getEnrolledStudents().size());
        assertTrue(course.getEnrolledStudents().contains(student1.getId()));
        assertTrue(course.getEnrolledStudents().contains(student2.getId()));
    }

    @Test
    void shouldNotEnrollNullStudent() {
        // given
        Course course = new Course("Database Systems", 5);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> course.enrollStudent(null));
    }

    @Test
    void shouldReturnCorrectStudentCount() {
        // given
        Course course = new Course("Algorithms", 6);
        Student student1 = new Student("Alice Brown");

        // when
        course.enrollStudent(student1);

        // then
        assertEquals(1, course.getEnrolledStudents().size());
    }

    @Test
    void shouldAllowMultipleEnrollmentsOfSameStudent() {
        // given
        Course course = new Course("Web Development", 4);
        Student student = new Student("Bob Wilson");

        // when
        course.enrollStudent(student);
        course.enrollStudent(student); // Enroll same student again

        // then
        assertEquals(2, course.getEnrolledStudents().size());
        assertTrue(course.getEnrolledStudents().contains(student.getId()));
    }

    @Test
    void shouldValidateEcts() {
        // given
        Course course = new Course("Test Course", 3);

        // when & then
        assertTrue(course.getEcts() > 0);
        assertEquals(3, course.getEcts());
    }
}
