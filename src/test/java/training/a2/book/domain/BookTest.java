package training.a2.book.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "978-0123456789", 300);
    }

    @Test
    void shouldCreateBookWithCorrectProperties() {
        assertNotNull(book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("978-0123456789", book.getIsbn());
        assertEquals(300, book.getPages());
        assertTrue(book.getAuthorIds().isEmpty());
    }

    @Test
    void shouldManageAuthors() {
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        
        // Test that getAuthorIds() returns immutable list
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthorIds().add(authorId1);
        });
        
        // Test removing from immutable list should also fail
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthorIds().remove(authorId2);
        });
    }

    @Test
    void shouldReturnCorrectAuthorCount() {
        assertEquals(0, book.getAuthorCount());
        
        // Simulate adding authors via business logic
        UUID authorId = UUID.randomUUID();
        book.addAuthor(authorId);
        assertEquals(1, book.getAuthorCount());
        
        book.removeAuthor(authorId);
        assertEquals(0, book.getAuthorCount());
    }

    @Test
    void shouldHandleAuthorRemoval() {
        UUID authorId = UUID.randomUUID();
        
        // Remove non-existing author should not crash
        book.removeAuthor(authorId);
        assertEquals(0, book.getAuthorCount());
    }

    @Test
    void shouldReturnImmutableAuthorsList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthorIds().clear();
        });
    }

    @Test
    void shouldHandleIsbnAndPagesCorrectly() {
        book.setIsbn("978-9876543210");
        book.setPages(500);
        
        assertEquals("978-9876543210", book.getIsbn());
        assertEquals(500, book.getPages());
    }

    @Test
    void shouldManageMultipleAuthors() {
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        UUID authorId3 = UUID.randomUUID();
        
        // Add multiple authors
        book.addAuthor(authorId1);
        book.addAuthor(authorId2);
        book.addAuthor(authorId3);
        
        assertEquals(3, book.getAuthorCount());
        assertTrue(book.getAuthorIds().contains(authorId1));
        assertTrue(book.getAuthorIds().contains(authorId2));
        assertTrue(book.getAuthorIds().contains(authorId3));
    }

    @Test
    void shouldPreventDuplicateAuthors() {
        UUID authorId = UUID.randomUUID();
        
        // Add same author twice
        book.addAuthor(authorId);
        book.addAuthor(authorId);
        
        // Should only appear once
        assertEquals(1, book.getAuthorCount());
        assertEquals(1, book.getAuthorIds().size());
    }

    @Test
    void shouldHandleNullAuthorOperations() {
        // Adding null should not crash or affect count
        book.addAuthor(null);
        assertEquals(0, book.getAuthorCount());
        
        // Removing null should not crash
        book.removeAuthor(null);
        assertEquals(0, book.getAuthorCount());
    }

    @Test
    void shouldSupportPagesCalculationForAuthors() {
        // Test that pages property is accessible for calculations
        assertEquals(300, book.getPages());
        
        // Change pages and verify
        book.setPages(250);
        assertEquals(250, book.getPages());
        
        book.setPages(450);
        assertEquals(450, book.getPages());
    }

    @Test
    void shouldMaintainAuthorListIntegrity() {
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        UUID authorId3 = UUID.randomUUID();
        
        // Add authors
        book.addAuthor(authorId1);
        book.addAuthor(authorId2);
        book.addAuthor(authorId3);
        
        // Remove one author
        book.removeAuthor(authorId2);
        
        assertEquals(2, book.getAuthorCount());
        assertTrue(book.getAuthorIds().contains(authorId1));
        assertFalse(book.getAuthorIds().contains(authorId2));
        assertTrue(book.getAuthorIds().contains(authorId3));
        
        // Remove non-existing author should not affect anything
        book.removeAuthor(UUID.randomUUID());
        assertEquals(2, book.getAuthorCount());
    }

    @Test
    void shouldHandleBookMetadata() {
        // Test title changes
        book.setTitle("Updated Title");
        assertEquals("Updated Title", book.getTitle());
        
        // Test ISBN validation scenarios (basic property testing)
        book.setIsbn("123-456-789");
        assertEquals("123-456-789", book.getIsbn());
        
        // Test pages boundary values
        book.setPages(1);
        assertEquals(1, book.getPages());
        
        book.setPages(1000);
        assertEquals(1000, book.getPages());
    }
}
