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
}
