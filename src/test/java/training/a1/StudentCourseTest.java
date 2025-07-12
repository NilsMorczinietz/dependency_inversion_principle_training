package training.a1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a1.course.application.CourseService;
import training.a1.course.domain.Course;
import training.a1.student.application.StudentService;
import training.a1.student.domain.Student;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class StudentCourseTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    private Student student1, student2, student3, student4;
    private Course course1, course2, course3;

    @BeforeEach
    public void setUp() {
        initializeStudents();
        initializeCourses();
        enrollStudents();
    }

    /**
     * Initializes the students used in the tests.
     */
    private void initializeStudents() {
        student1 = new Student("Alice Smith");
        student1 = studentService.addStudent(student1);
        
        student2 = new Student("Bob Johnson");
        student2 = studentService.addStudent(student2);
        
        student3 = new Student("Charlie Brown");
        student3 = studentService.addStudent(student3);
        
        student4 = new Student("Diana Wilson");
        student4 = studentService.addStudent(student4);
    }

    /**
     * Initializes the courses used in the tests.
     */
    private void initializeCourses() {
        course1 = new Course("Software Engineering", 6);
        course1 = courseService.addCourse(course1);
        
        course2 = new Course("Data Structures", 8);
        course2 = courseService.addCourse(course2);
        
        course3 = new Course("Machine Learning", 10);
        course3 = courseService.addCourse(course3);
    }

    /**
     * Enrolls students in courses to establish relationships.
     */
    private void enrollStudents() {
        // All students enroll in Software Engineering
        courseService.enrollStudentInCourse(student1, course1);
        courseService.enrollStudentInCourse(student2, course1);
        courseService.enrollStudentInCourse(student3, course1);
        courseService.enrollStudentInCourse(student4, course1);
        
        // Some students enroll in Data Structures
        courseService.enrollStudentInCourse(student1, course2);
        courseService.enrollStudentInCourse(student2, course2);
        
        // One student enrolls in Machine Learning
        courseService.enrollStudentInCourse(student1, course3);
    }

    @Test
    public void testEctsLoadForStudents() {
        // given, when, then
        // Student1: Software Engineering (6) + Data Structures (8) + Machine Learning (10) = 24 ECTS
        assertEquals(24, studentService.ectsLoadForStudent(student1.getId()));
        
        // Student2: Software Engineering (6) + Data Structures (8) = 14 ECTS
        assertEquals(14, studentService.ectsLoadForStudent(student2.getId()));
        
        // Student3: Software Engineering (6) = 6 ECTS
        assertEquals(6, studentService.ectsLoadForStudent(student3.getId()));
        
        // Student4: Software Engineering (6) = 6 ECTS
        assertEquals(6, studentService.ectsLoadForStudent(student4.getId()));
    }

    @Test
    public void testAverageEctsLoad() {
        // Total ECTS: 24 + 14 + 6 + 6 = 50, Average = 50 / 4 = 12.5
        assertEquals(12.5f, studentService.averageEctsLoad(), 0.01f);
    }

    @Test
    public void testCoursesForStudent() {
        // Test courses for student1 (should be enrolled in all 3 courses)
        var student1Courses = courseService.coursesForStudent(student1.getId());
        assertEquals(3, student1Courses.size());
        assertTrue(student1Courses.stream().anyMatch(course -> course.getName().equals("Software Engineering")));
        assertTrue(student1Courses.stream().anyMatch(course -> course.getName().equals("Data Structures")));
        assertTrue(student1Courses.stream().anyMatch(course -> course.getName().equals("Machine Learning")));
        
        // Test courses for student2 (should be enrolled in 2 courses)
        var student2Courses = courseService.coursesForStudent(student2.getId());
        assertEquals(2, student2Courses.size());
        assertTrue(student2Courses.stream().anyMatch(course -> course.getName().equals("Software Engineering")));
        assertTrue(student2Courses.stream().anyMatch(course -> course.getName().equals("Data Structures")));
        
        // Test courses for student3 (should be enrolled in 1 course)
        var student3Courses = courseService.coursesForStudent(student3.getId());
        assertEquals(1, student3Courses.size());
        assertEquals("Software Engineering", student3Courses.get(0).getName());
    }

    @Test
    public void testStudentEnrollmentCounts() {
        // Software Engineering should have 4 students enrolled
        assertEquals(4, course1.getEnrolledStudents().size());
        
        // Data Structures should have 2 students enrolled
        assertEquals(2, course2.getEnrolledStudents().size());
        
        // Machine Learning should have 1 student enrolled
        assertEquals(1, course3.getEnrolledStudents().size());
    }

    @Test
    public void testCourseEctsPoints() {
        // Verify ECTS points for each course
        assertEquals(6, course1.getEcts());
        assertEquals(8, course2.getEcts());
        assertEquals(10, course3.getEcts());
    }

    @Test
    public void testStudentAndCourseNames() {
        // Verify student names
        assertEquals("Alice Smith", student1.getName());
        assertEquals("Bob Johnson", student2.getName());
        assertEquals("Charlie Brown", student3.getName());
        assertEquals("Diana Wilson", student4.getName());
        
        // Verify course names
        assertEquals("Software Engineering", course1.getName());
        assertEquals("Data Structures", course2.getName());
        assertEquals("Machine Learning", course3.getName());
    }
}
