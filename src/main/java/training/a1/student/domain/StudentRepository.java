package training.a1.student.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, StudentId> {
    List<Student> findAll();
}
