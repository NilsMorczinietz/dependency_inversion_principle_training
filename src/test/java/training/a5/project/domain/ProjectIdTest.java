package training.a5.project.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectIdTest {

    @Test
    void shouldCreateProjectIdWithRandomUUID() {
        // when
        ProjectId projectId = new ProjectId();

        // then
        assertNotNull(projectId.getId());
    }

    @Test
    void shouldCreateProjectIdWithSpecificUUID() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        ProjectId projectId = new ProjectId(uuid);

        // then
        assertEquals(uuid, projectId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        UUID uuid = UUID.randomUUID();
        ProjectId projectId1 = new ProjectId(uuid);
        ProjectId projectId2 = new ProjectId(uuid);
        ProjectId projectId3 = new ProjectId();

        // then
        assertEquals(projectId1, projectId2);
        assertEquals(projectId1.hashCode(), projectId2.hashCode());
        assertNotEquals(projectId1, projectId3);
    }

    @Test
    void shouldImplementToString() {
        // given
        ProjectId projectId = new ProjectId();

        // when
        String toString = projectId.toString();

        // then
        assertNotNull(toString);
        assertTrue(toString.contains(projectId.getId().toString()));
    }

    @Test
    void shouldNotBeEqualToNull() {
        // given
        ProjectId projectId = new ProjectId();

        // then
        assertNotEquals(projectId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        // given
        ProjectId projectId = new ProjectId();
        String notAProjectId = "not a project id";

        // then
        assertNotEquals(projectId, notAProjectId);
    }
}
