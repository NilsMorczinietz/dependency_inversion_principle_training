package training.a1.course.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.a1.course.domain.Course;
import training.a1.course.domain.CourseId;
import training.a1.course.domain.CourseRepository;
import training.a1.student.domain.Student;
import training.a1.student.domain.StudentId;
import training.a1.student.domain.StudentRepository;

import java.util.List;

@Service
public class CourseService {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            StudentRepository studentRepository
    ) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public Course addCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course is null");
        return courseRepository.save(course);
    }


    public Course getCourseById(CourseId courseId) {
        if (courseId == null) throw new IllegalArgumentException("Course ID is null or empty");
        return courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course not found"));
    }


    public void enrollStudentInCourse(Student student, Course course) {
        if (student == null || course == null)
            throw new IllegalArgumentException("Course or student is null");
        course.enrollStudent(student);
        courseRepository.save(course);
    }


    public List<Course> coursesForStudent(StudentId studentId) {
        if (studentId == null) throw new IllegalArgumentException("Student is null");
        return courseRepository.findByEnrolledStudentsContains(studentId);
    }
}
