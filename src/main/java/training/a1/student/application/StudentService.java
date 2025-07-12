package training.a1.student.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.a1.student.domain.Student;
import training.a1.student.domain.StudentId;
import training.a1.student.domain.StudentRepository;
import training.a1.course.application.CourseService;
import training.a1.course.domain.Course;

import java.util.List;


@Service
public class StudentService {
    private StudentRepository studentRepository;
    private CourseService courseService;

    @Autowired
    public StudentService( StudentRepository studentRepository, CourseService courseService ) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
    }

    public Student addStudent( Student student) {
        if ( student == null ) throw new IllegalArgumentException( "Student is null" );
        return studentRepository.save( student );
    }


    public Student getStudentById( StudentId studentId ) {
        if ( studentId == null ) throw new IllegalArgumentException( "Student ID is null or empty" );
        return studentRepository.findById( studentId ).orElseThrow(
                () -> new IllegalArgumentException( "Student not found" ) );
    }


    public Float averageEctsLoad() {
        List<Student> students = studentRepository.findAll();
        Float sum = 0f;
        for ( Student student : students )
            sum += ectsLoadForStudent( student.getId() );
        return sum / Float.valueOf( students.size() );
    }


    public Integer ectsLoadForStudent( StudentId studentId ) {
        if ( studentId == null ) throw new IllegalArgumentException( "StudentId is null" );
        List<Course> courses = courseService.coursesForStudent( studentId );
        Integer ects = 0;
        for ( Course course : courses )
            ects += course.getEcts();
        return ects;
    }
}