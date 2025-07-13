package training.a5.developer.domain;

import org.springframework.data.repository.CrudRepository;
import training.a5.project.domain.ProjectId;

import java.util.List;

public interface DeveloperRepository extends CrudRepository<Developer, DeveloperId> {
    List<Developer> findAll();
    List<Developer> findByAssignedProjectsContains(ProjectId projectId);
}
