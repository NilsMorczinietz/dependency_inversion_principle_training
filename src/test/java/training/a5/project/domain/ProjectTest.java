package training.a5.project.domain;

import org.junit.jupiter.api.Test;
import training.a5.developer.domain.Developer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void shouldCreateProjectWithValidData() {
        // given
        String name = "Test Project";
        String description = "Test Description";
        BigDecimal budget = new BigDecimal("100000");
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        // when
        Project project = new Project(name, description, budget, startDate, endDate);

        // then
        assertNotNull(project.getId());
        assertEquals(name, project.getName());
        assertEquals(description, project.getDescription());
        assertEquals(budget, project.getBudget());
        assertEquals(startDate, project.getStartDate());
        assertEquals(endDate, project.getEndDate());
        assertTrue(project.getTechnologies().isEmpty());
        assertTrue(project.getAssignedDevelopers().isEmpty());
    }

    @Test
    void shouldAssignDevelopers() {
        // given
        Project project = new Project("Test Project", "Description", 
                                    new BigDecimal("50000"), 
                                    LocalDate.now(), 
                                    LocalDate.now().plusMonths(6));
        Developer developer1 = new Developer("John Doe", "john@test.com", "Senior", new BigDecimal("80"));
        Developer developer2 = new Developer("Jane Smith", "jane@test.com", "Mid", new BigDecimal("60"));

        // when
        project.assignDeveloper(developer1);
        project.assignDeveloper(developer2);

        // then
        assertEquals(2, project.getAssignedDevelopers().size());
        assertTrue(project.getAssignedDevelopers().contains(developer1.getId()));
        assertTrue(project.getAssignedDevelopers().contains(developer2.getId()));
        assertEquals(2, project.getDeveloperCount());
    }

    @Test
    void shouldNotAssignNullDeveloper() {
        // given
        Project project = new Project("Test Project", "Description", 
                                    new BigDecimal("50000"), 
                                    LocalDate.now(), 
                                    LocalDate.now().plusMonths(6));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> project.assignDeveloper(null));
    }

    @Test
    void shouldAddTechnologies() {
        // given
        Project project = new Project("Test Project", "Description", 
                                    new BigDecimal("50000"), 
                                    LocalDate.now(), 
                                    LocalDate.now().plusMonths(6));

        // when
        project.addTechnology("Java");
        project.addTechnology("Spring");
        project.addTechnology("Java"); // Should not be added twice

        // then
        assertEquals(2, project.getTechnologies().size());
        assertTrue(project.getTechnologies().contains("Java"));
        assertTrue(project.getTechnologies().contains("Spring"));
    }

    @Test
    void shouldValidateProjectActivity() {
        // given
        LocalDate past = LocalDate.now().minusMonths(1);
        LocalDate future = LocalDate.now().plusMonths(1);
        
        Project activeProject = new Project("Active Project", "Description", 
                                          new BigDecimal("50000"), past, future);
        
        Project futureProject = new Project("Future Project", "Description", 
                                          new BigDecimal("50000"), future, future.plusMonths(1));

        // when & then
        assertTrue(activeProject.isActive());
        assertFalse(futureProject.isActive());
    }

    @Test
    void shouldNotCreateProjectWithInvalidData() {
        // when & then
        assertThrows(IllegalArgumentException.class, 
                    () -> new Project(null, "Description", new BigDecimal("50000"), 
                                    LocalDate.now(), LocalDate.now().plusMonths(6)));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Project("", "Description", new BigDecimal("50000"), 
                                    LocalDate.now(), LocalDate.now().plusMonths(6)));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Project("Test", "Description", new BigDecimal("-1000"), 
                                    LocalDate.now(), LocalDate.now().plusMonths(6)));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Project("Test", "Description", new BigDecimal("50000"), 
                                    null, LocalDate.now().plusMonths(6)));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        Project project1 = new Project("Test Project", "Description", 
                                     new BigDecimal("50000"), 
                                     LocalDate.now(), 
                                     LocalDate.now().plusMonths(6));
        Project project2 = new Project("Another Project", "Description", 
                                     new BigDecimal("75000"), 
                                     LocalDate.now(), 
                                     LocalDate.now().plusMonths(6));

        // then
        assertEquals(project1, project1); // reflexive
        assertNotEquals(project1, project2);
        assertEquals(project1.hashCode(), project1.hashCode());
    }
}
