package training.a5.project.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a5.project.domain.Project;
import training.a5.project.domain.ProjectId;
import training.a5.project.domain.ProjectRepository;
import training.a5.developer.domain.Developer;
import training.a5.developer.domain.DeveloperId;
import training.a5.developer.domain.DeveloperRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;
    private DeveloperRepository developerRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, DeveloperRepository developerRepository) {
        this.projectRepository = projectRepository;
        this.developerRepository = developerRepository;
    }

    public Project addProject(Project project) {
        if (project == null) throw new IllegalArgumentException("Project is null");
        return projectRepository.save(project);
    }

    public Project getProjectById(ProjectId projectId) {
        if (projectId == null) throw new IllegalArgumentException("Project ID is null");
        return projectRepository.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("Project not found"));
    }

    public void assignDeveloperToProject(Developer developer, Project project) {
        if (developer == null || project == null)
            throw new IllegalArgumentException("Developer or project is null");
        project.assignDeveloper(developer);
        developer.assignToProject(project);
        projectRepository.save(project);
        developerRepository.save(developer);
    }

    public List<Project> projectsForDeveloper(DeveloperId developerId) {
        if (developerId == null) throw new IllegalArgumentException("Developer ID is null");
        return projectRepository.findByAssignedDevelopersContains(developerId);
    }

    public BigDecimal averageProjectBudget() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) return BigDecimal.ZERO;
        
        BigDecimal totalBudget = BigDecimal.ZERO;
        for (Project project : projects) {
            totalBudget = totalBudget.add(project.getBudget());
        }
        return totalBudget.divide(BigDecimal.valueOf(projects.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    public int averageDevelopersPerProject() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) return 0;
        
        int totalDevelopers = 0;
        for (Project project : projects) {
            totalDevelopers += project.getDeveloperCount();
        }
        return totalDevelopers / projects.size();
    }
}
