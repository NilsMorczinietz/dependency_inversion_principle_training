package training.a5.developer.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a5.project.domain.Project;
import training.a5.project.domain.ProjectId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Developer {
    
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private DeveloperId id;

    // the name of the developer
    private String name;

    // the email of the developer
    private String email;

    // the experience level (Junior, Mid, Senior)
    private String experienceLevel;

    // the hourly rate
    private BigDecimal hourlyRate;

    // the programming skills
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills = new ArrayList<>();

    // the assigned projects
    @ElementCollection(fetch = FetchType.EAGER)
    private final List<ProjectId> assignedProjects = new ArrayList<>();

    public Developer(String name, String email, String experienceLevel, BigDecimal hourlyRate) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Developer name cannot be null or empty");
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be null or empty");
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        
        this.name = name;
        this.email = email;
        this.experienceLevel = experienceLevel;
        this.hourlyRate = hourlyRate;
        this.id = new DeveloperId();
    }

    public void assignToProject(Project project) {
        if (project == null) throw new IllegalArgumentException("Project is null");
        assignedProjects.add(project.getId());
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.isEmpty() && !skills.contains(skill)) {
            skills.add(skill);
        }
    }

    public boolean hasSkill(String skill) {
        return skills.contains(skill);
    }

    public int getProjectCount() {
        return assignedProjects.size();
    }

    public boolean isSenior() {
        return "Senior".equalsIgnoreCase(experienceLevel);
    }
}
