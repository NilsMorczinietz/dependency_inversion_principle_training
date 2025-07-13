package training.a5.developer.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeveloperIdTest {

    @Test
    void shouldCreateDeveloperIdWithRandomUUID() {
        // when
        DeveloperId developerId = new DeveloperId();

        // then
        assertNotNull(developerId.getId());
    }

    @Test
    void shouldCreateDeveloperIdWithSpecificUUID() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        DeveloperId developerId = new DeveloperId(uuid);

        // then
        assertEquals(uuid, developerId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        UUID uuid = UUID.randomUUID();
        DeveloperId developerId1 = new DeveloperId(uuid);
        DeveloperId developerId2 = new DeveloperId(uuid);
        DeveloperId developerId3 = new DeveloperId();

        // then
        assertEquals(developerId1, developerId2);
        assertEquals(developerId1.hashCode(), developerId2.hashCode());
        assertNotEquals(developerId1, developerId3);
    }

    @Test
    void shouldImplementToString() {
        // given
        DeveloperId developerId = new DeveloperId();

        // when
        String toString = developerId.toString();

        // then
        assertNotNull(toString);
        assertTrue(toString.contains(developerId.getId().toString()));
    }

    @Test
    void shouldNotBeEqualToNull() {
        // given
        DeveloperId developerId = new DeveloperId();

        // then
        assertNotEquals(developerId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        // given
        DeveloperId developerId = new DeveloperId();
        String notADeveloperId = "not a developer id";

        // then
        assertNotEquals(developerId, notADeveloperId);
    }
}
