package training.a5.developer.domain;

import org.junit.jupiter.api.Test;
import training.a5.project.domain.Project;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeveloperTest {

    @Test
    void shouldCreateDeveloperWithValidData() {
        // given
        String name = "John Doe";
        String email = "john@test.com";
        String experienceLevel = "Senior";
        BigDecimal hourlyRate = new BigDecimal("80");

        // when
        Developer developer = new Developer(name, email, experienceLevel, hourlyRate);

        // then
        assertNotNull(developer.getId());
        assertEquals(name, developer.getName());
        assertEquals(email, developer.getEmail());
        assertEquals(experienceLevel, developer.getExperienceLevel());
        assertEquals(hourlyRate, developer.getHourlyRate());
        assertTrue(developer.getSkills().isEmpty());
        assertTrue(developer.getAssignedProjects().isEmpty());
    }

    @Test
    void shouldAssignToProjects() {
        // given
        Developer developer = new Developer("Jane Smith", "jane@test.com", "Mid", new BigDecimal("60"));
        Project project1 = new Project("Project 1", "Description", 
                                     new BigDecimal("50000"), 
                                     LocalDate.now(), 
                                     LocalDate.now().plusMonths(6));
        Project project2 = new Project("Project 2", "Description", 
                                     new BigDecimal("75000"), 
                                     LocalDate.now(), 
                                     LocalDate.now().plusMonths(6));

        // when
        developer.assignToProject(project1);
        developer.assignToProject(project2);

        // then
        assertEquals(2, developer.getAssignedProjects().size());
        assertTrue(developer.getAssignedProjects().contains(project1.getId()));
        assertTrue(developer.getAssignedProjects().contains(project2.getId()));
        assertEquals(2, developer.getProjectCount());
    }

    @Test
    void shouldNotAssignToNullProject() {
        // given
        Developer developer = new Developer("Jane Smith", "jane@test.com", "Mid", new BigDecimal("60"));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> developer.assignToProject(null));
    }

    @Test
    void shouldAddSkills() {
        // given
        Developer developer = new Developer("Bob Wilson", "bob@test.com", "Junior", new BigDecimal("40"));

        // when
        developer.addSkill("Java");
        developer.addSkill("Spring");
        developer.addSkill("Java"); // Should not be added twice

        // then
        assertEquals(2, developer.getSkills().size());
        assertTrue(developer.hasSkill("Java"));
        assertTrue(developer.hasSkill("Spring"));
        assertFalse(developer.hasSkill("Python"));
    }

    @Test
    void shouldIdentifySeniorDeveloper() {
        // given
        Developer senior = new Developer("Senior Dev", "senior@test.com", "Senior", new BigDecimal("90"));
        Developer junior = new Developer("Junior Dev", "junior@test.com", "Junior", new BigDecimal("40"));

        // when & then
        assertTrue(senior.isSenior());
        assertFalse(junior.isSenior());
    }

    @Test
    void shouldNotCreateDeveloperWithInvalidData() {
        // when & then
        assertThrows(IllegalArgumentException.class, 
                    () -> new Developer(null, "test@test.com", "Mid", new BigDecimal("60")));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Developer("", "test@test.com", "Mid", new BigDecimal("60")));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Developer("Test", null, "Mid", new BigDecimal("60")));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Developer("Test", "", "Mid", new BigDecimal("60")));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> new Developer("Test", "test@test.com", "Mid", new BigDecimal("-10")));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        Developer developer1 = new Developer("Alice Johnson", "alice@test.com", "Senior", new BigDecimal("80"));
        Developer developer2 = new Developer("Bob Smith", "bob@test.com", "Mid", new BigDecimal("60"));

        // then
        assertEquals(developer1, developer1); // reflexive
        assertNotEquals(developer1, developer2);
        assertEquals(developer1.hashCode(), developer1.hashCode());
    }
}
