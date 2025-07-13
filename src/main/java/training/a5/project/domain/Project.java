package training.a5.project.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a5.developer.domain.Developer;
import training.a5.developer.domain.DeveloperId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Project {
    
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private ProjectId id;

    public void setId(UUID id){
        this.id = new ProjectId(id);
    }
    public void setId(ProjectId id){
        this.id = id;
    }

    // the name of the project
    private String name;

    // the description of the project
    private String description;

    // the budget of the project
    private BigDecimal budget;

    // the start date of the project
    private LocalDate startDate;

    // the end date of the project
    private LocalDate endDate;

    // the technology stack used in the project
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> technologies = new ArrayList<>();

    // the assigned developers
    @ElementCollection(fetch = FetchType.EAGER)
    private final List<DeveloperId> assignedDevelopers = new ArrayList<>();

    public Project(String name, String description, BigDecimal budget, LocalDate startDate, LocalDate endDate) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Project name cannot be null or empty");
        if (budget == null || budget.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Budget cannot be negative");
        if (startDate == null)
            throw new IllegalArgumentException("Start date cannot be null");
        
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = new ProjectId();
    }

    public void assignDeveloper(Developer developer) {
        if (developer == null) throw new IllegalArgumentException("Developer is null");
        assignedDevelopers.add(developer.getId());
    }

    public void addTechnology(String technology) {
        if (technology != null && !technology.isEmpty() && !technologies.contains(technology)) {
            technologies.add(technology);
        }
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (startDate.isBefore(now) || startDate.isEqual(now)) && 
               (endDate == null || endDate.isAfter(now) || endDate.isEqual(now));
    }

    public int getDeveloperCount() {
        return assignedDevelopers.size();
    }
}
