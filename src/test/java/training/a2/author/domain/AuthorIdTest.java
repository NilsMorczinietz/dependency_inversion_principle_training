package training.a2.author.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorIdTest {

    @Test
    void shouldCreateAuthorIdWithUUID() {
        UUID uuid = UUID.randomUUID();
        AuthorId authorId = new AuthorId(uuid);
        
        assertEquals(uuid, authorId.getId());
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        AuthorId authorId1 = new AuthorId(uuid);
        AuthorId authorId2 = new AuthorId(uuid);
        AuthorId authorId3 = new AuthorId(UUID.randomUUID());
        
        assertEquals(authorId1, authorId2);
        assertNotEquals(authorId1, authorId3);
        assertEquals(authorId1.hashCode(), authorId2.hashCode());
    }

    @Test
    void shouldHaveCorrectToString() {
        UUID uuid = UUID.randomUUID();
        AuthorId authorId = new AuthorId(uuid);
        
        assertTrue(authorId.toString().contains(uuid.toString()));
    }

    @Test
    void shouldHandleNullComparisons() {
        AuthorId authorId = new AuthorId(UUID.randomUUID());
        
        assertNotEquals(authorId, null);
        assertNotEquals(authorId, "not an AuthorId");
    }
}
