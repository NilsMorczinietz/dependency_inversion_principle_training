package training.a2.author.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import training.a2.book.domain.BookId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    private Author author;
    private AuthorId authorId;

    @BeforeEach
    void setUp() {
        authorId = new AuthorId(UUID.randomUUID());
        author = new Author("John", "Doe", "john.doe@example.com");
        author.setId(authorId.getId());
    }

    @Test
    void shouldCreateAuthorWithCorrectProperties() {
        assertEquals(authorId, author.getId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
        assertEquals("john.doe@example.com", author.getEmail());
        assertEquals("John Doe", author.getFullName());
        assertTrue(author.getPublishedBooks().isEmpty());
    }

    @Test
    void shouldManageBooks() {
        BookId bookId1 = new BookId(UUID.randomUUID());
        BookId bookId2 = new BookId(UUID.randomUUID());
        
        // Test that getPublishedBooks() returns immutable list
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBooks().add(bookId1);
        });
        
        // Test removing from immutable list should also fail
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBooks().remove(bookId2);
        });
    }

    @Test
    void shouldReturnCorrectBookCount() {
        assertEquals(0, author.getBookCount());
        
        // Simulate adding books via business logic
        BookId bookId = new BookId(UUID.randomUUID());
        author.removeBook(bookId); // Should not crash on empty list
        
        assertEquals(0, author.getBookCount());
    }

    @Test
    void shouldHandleBookRemoval() {
        BookId bookId = new BookId(UUID.randomUUID());
        
        // Remove non-existing book should not crash
        author.removeBook(bookId);
        assertEquals(0, author.getBookCount());
    }

    @Test
    void shouldReturnImmutableBooksList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            author.getPublishedBooks().clear();
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
