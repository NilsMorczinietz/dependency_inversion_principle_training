package training.a2.book.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookIdTest {

    @Test
    void shouldCreateBookIdWithUUID() {
        UUID uuid = UUID.randomUUID();
        BookId bookId = new BookId(uuid);
        
        assertEquals(uuid, bookId.getId());
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        BookId bookId1 = new BookId(uuid);
        BookId bookId2 = new BookId(uuid);
        BookId bookId3 = new BookId(UUID.randomUUID());
        
        assertEquals(bookId1, bookId2);
        assertNotEquals(bookId1, bookId3);
        assertEquals(bookId1.hashCode(), bookId2.hashCode());
    }

    @Test
    void shouldHaveCorrectToString() {
        UUID uuid = UUID.randomUUID();
        BookId bookId = new BookId(uuid);
        
        assertTrue(bookId.toString().contains(uuid.toString()));
    }

    @Test
    void shouldHandleNullComparisons() {
        BookId bookId = new BookId(UUID.randomUUID());
        
        assertNotEquals(bookId, null);
        assertNotEquals(bookId, "not a BookId");
    }
}
