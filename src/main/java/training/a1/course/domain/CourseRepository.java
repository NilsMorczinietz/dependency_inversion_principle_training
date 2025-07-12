package training.a1.course.domain;

import org.springframework.data.repository.CrudRepository;
import training.a1.student.domain.StudentId;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, CourseId> {
    List<Course> findByEnrolledStudentsContains( StudentId studentId );
}
