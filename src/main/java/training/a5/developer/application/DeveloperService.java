package training.a5.developer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a5.developer.domain.Developer;
import training.a5.developer.domain.DeveloperId;
import training.a5.developer.domain.DeveloperRepository;
import training.a5.project.application.ProjectService;
import training.a5.project.domain.Project;
import training.a5.project.domain.ProjectId;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DeveloperService {
    private DeveloperRepository developerRepository;
    private ProjectService projectService;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository, ProjectService projectService) {
        this.developerRepository = developerRepository;
        this.projectService = projectService;
    }

    public Developer addDeveloper(Developer developer) {
        if (developer == null) throw new IllegalArgumentException("Developer is null");
        return developerRepository.save(developer);
    }

    public Developer getDeveloperById(DeveloperId developerId) {
        if (developerId == null) throw new IllegalArgumentException("Developer ID is null");
        return developerRepository.findById(developerId).orElseThrow(
                () -> new IllegalArgumentException("Developer not found"));
    }

    public List<Developer> developersForProject(ProjectId projectId) {
        if (projectId == null) throw new IllegalArgumentException("Project ID is null");
        return developerRepository.findByAssignedProjectsContains(projectId);
    }

    public BigDecimal totalProjectBudgetForDeveloper(DeveloperId developerId) {
        if (developerId == null) throw new IllegalArgumentException("Developer ID is null");
        List<Project> projects = projectService.projectsForDeveloper(developerId);
        BigDecimal totalBudget = BigDecimal.ZERO;
        for (Project project : projects) {
            totalBudget = totalBudget.add(project.getBudget());
        }
        return totalBudget;
    }

    public BigDecimal averageHourlyRate() {
        List<Developer> developers = developerRepository.findAll();
        if (developers.isEmpty()) return BigDecimal.ZERO;
        
        BigDecimal totalRate = BigDecimal.ZERO;
        for (Developer developer : developers) {
            totalRate = totalRate.add(developer.getHourlyRate());
        }
        return totalRate.divide(BigDecimal.valueOf(developers.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    public int averageProjectsPerDeveloper() {
        List<Developer> developers = developerRepository.findAll();
        if (developers.isEmpty()) return 0;
        
        int totalProjects = 0;
        for (Developer developer : developers) {
            totalProjects += developer.getProjectCount();
        }
        return totalProjects / developers.size();
    }
}
