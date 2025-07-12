package training.a2.book.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import training.a2.author.domain.AuthorId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;
    private BookId bookId;

    @BeforeEach
    void setUp() {
        bookId = new BookId(UUID.randomUUID());
        book = new Book("Test Book", "978-0123456789", 300);
        book.setId(bookId.getId());
    }

    @Test
    void shouldCreateBookWithCorrectProperties() {
        assertEquals(bookId, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("978-0123456789", book.getIsbn());
        assertEquals(300, book.getPages());
        assertTrue(book.getAuthors().isEmpty());
    }

    @Test
    void shouldManageAuthors() {
        AuthorId authorId1 = new AuthorId(UUID.randomUUID());
        AuthorId authorId2 = new AuthorId(UUID.randomUUID());
        
        // Test that getAuthors() returns immutable list
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthors().add(authorId1);
        });
        
        // Test removing from immutable list should also fail
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthors().remove(authorId2);
        });
    }

    @Test
    void shouldReturnCorrectAuthorCount() {
        assertEquals(0, book.getAuthorCount());
        
        // Simulate adding authors via business logic
        AuthorId authorId = new AuthorId(UUID.randomUUID());
        book.removeAuthor(authorId); // Should not crash on empty list
        
        assertEquals(0, book.getAuthorCount());
    }

    @Test
    void shouldHandleAuthorRemoval() {
        AuthorId authorId = new AuthorId(UUID.randomUUID());
        
        // Remove non-existing author should not crash
        book.removeAuthor(authorId);
        assertEquals(0, book.getAuthorCount());
    }

    @Test
    void shouldReturnImmutableAuthorsList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            book.getAuthors().clear();
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
