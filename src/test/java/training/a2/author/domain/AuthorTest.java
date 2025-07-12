package training.a2.author.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("John", "Doe", "john.doe@example.com");
    }

    @Test
    void shouldCreateAuthorWithCorrectProperties() {
        assertNotNull(author.getId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
        assertEquals("john.doe@example.com", author.getEmail());
        assertEquals("John Doe", author.getFullName());
        assertTrue(author.getPublishedBookIds().isEmpty());
    }

    @Test
    void shouldManageBooks() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();
        
        // Test that getPublishedBookIds() returns immutable list
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBookIds().add(bookId1);
        });
        
        // Test removing from immutable list should also fail
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBookIds().remove(bookId2);
        });
    }

    @Test
    void shouldReturnCorrectBookCount() {
        assertEquals(0, author.getBookCount());
        
        // Simulate adding books via business logic
        UUID bookId = UUID.randomUUID();
        author.addBook(bookId);
        assertEquals(1, author.getBookCount());
        
        author.removeBook(bookId);
        assertEquals(0, author.getBookCount());
    }

    @Test
    void shouldHandleBookRemoval() {
        UUID bookId = UUID.randomUUID();
        
        // Remove non-existing book should not crash
        author.removeBook(bookId);
        assertEquals(0, author.getBookCount());
    }

    @Test
    void shouldReturnImmutableBooksList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBookIds().clear();
        });
    }

    @Test
    void shouldHandleNameChanges() {
        author.setFirstName("Jane");
        author.setLastName("Smith");
        
        assertEquals("Jane", author.getFirstName());
        assertEquals("Smith", author.getLastName());
        assertEquals("Jane Smith", author.getFullName());
    }

    @Test
    void shouldHandleEmailChanges() {
        author.setEmail("jane.smith@example.com");
        
        assertEquals("jane.smith@example.com", author.getEmail());
    }
}
