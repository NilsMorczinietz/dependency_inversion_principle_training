package training.a5.project.domain;

import org.springframework.data.repository.CrudRepository;
import training.a5.developer.domain.DeveloperId;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, ProjectId> {
    List<Project> findAll();
    List<Project> findByAssignedDevelopersContains(DeveloperId developerId);
}
