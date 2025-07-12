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

    @Test
    void shouldManageMultipleBooks() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();
        UUID bookId3 = UUID.randomUUID();
        
        // Add multiple books
        author.addBook(bookId1);
        author.addBook(bookId2);
        author.addBook(bookId3);
        
        assertEquals(3, author.getBookCount());
        assertTrue(author.getPublishedBookIds().contains(bookId1));
        assertTrue(author.getPublishedBookIds().contains(bookId2));
        assertTrue(author.getPublishedBookIds().contains(bookId3));
    }

    @Test
    void shouldPreventDuplicateBooks() {
        UUID bookId = UUID.randomUUID();
        
        // Add same book twice
        author.addBook(bookId);
        author.addBook(bookId);
        
        // Should only appear once
        assertEquals(1, author.getBookCount());
        assertEquals(1, author.getPublishedBookIds().size());
    }

    @Test
    void shouldHandleNullBookOperations() {
        // Adding null should not crash or affect count
        author.addBook(null);
        assertEquals(0, author.getBookCount());
        
        // Removing null should not crash
        author.removeBook(null);
        assertEquals(0, author.getBookCount());
    }

    @Test
    void shouldSupportProductivityAssessment() {
        assertEquals(0, author.getBookCount());
        
        // Author with no books is not productive
        assertFalse(author.getBookCount() >= 3);
        
        // Add books to make author productive
        author.addBook(UUID.randomUUID());
        author.addBook(UUID.randomUUID());
        assertEquals(2, author.getBookCount());
        assertFalse(author.getBookCount() >= 3); // Still not productive
        
        author.addBook(UUID.randomUUID());
        assertEquals(3, author.getBookCount());
        assertTrue(author.getBookCount() >= 3); // Now productive
    }

    @Test
    void shouldMaintainBookListIntegrity() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();
        UUID bookId3 = UUID.randomUUID();
        
        // Add books
        author.addBook(bookId1);
        author.addBook(bookId2);
        author.addBook(bookId3);
        
        // Remove one book
        author.removeBook(bookId2);
        
        assertEquals(2, author.getBookCount());
        assertTrue(author.getPublishedBookIds().contains(bookId1));
        assertFalse(author.getPublishedBookIds().contains(bookId2));
        assertTrue(author.getPublishedBookIds().contains(bookId3));
        
        // Remove non-existing book should not affect anything
        author.removeBook(UUID.randomUUID());
        assertEquals(2, author.getBookCount());
    }
}
