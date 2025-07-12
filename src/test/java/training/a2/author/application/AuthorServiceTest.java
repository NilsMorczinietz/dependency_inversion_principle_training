package training.a2.author.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import training.a2.author.domain.AuthorRepository;
import training.a2.book.application.BookService;
import training.a2.book.domain.Book;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, bookService);
    }

    @Test
    void shouldReturnCorrectBookCountForAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        Book book3 = new Book("Book 3", "ISBN3", 150);
        
        List<Book> authorBooks = Arrays.asList(book1, book2, book3);
        
        when(bookService.getBooksForAuthor(authorId)).thenReturn(authorBooks);

        // When
        int bookCount = authorService.getBookCountForAuthor(authorId);

        // Then
        assertEquals(3, bookCount);
    }

    @Test
    void shouldReturnZeroWhenAuthorHasNoBooks() {
        // Given
        UUID authorId = UUID.randomUUID();
        
        when(bookService.getBooksForAuthor(authorId)).thenReturn(Collections.emptyList());

        // When
        int bookCount = authorService.getBookCountForAuthor(authorId);

        // Then
        assertEquals(0, bookCount);
    }

    @Test
    void shouldReturnCorrectTotalPagesForAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        int expectedTotalPages = 650; // 200 + 300 + 150
        
        when(bookService.getTotalPagesForAuthor(authorId)).thenReturn(expectedTotalPages);

        // When
        int totalPages = authorService.getTotalPagesWrittenByAuthor(authorId);

        // Then
        assertEquals(expectedTotalPages, totalPages);
    }

    @Test
    void shouldReturnZeroWhenAuthorHasNoPagesWritten() {
        // Given
        UUID authorId = UUID.randomUUID();
        
        when(bookService.getTotalPagesForAuthor(authorId)).thenReturn(0);

        // When
        int totalPages = authorService.getTotalPagesWrittenByAuthor(authorId);

        // Then
        assertEquals(0, totalPages);
    }

    @Test
    void shouldIdentifyProductiveAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        Book book3 = new Book("Book 3", "ISBN3", 150);
        
        List<Book> authorBooks = Arrays.asList(book1, book2, book3);
        
        when(bookService.getBooksForAuthor(authorId)).thenReturn(authorBooks);

        // When
        boolean isProductive = authorService.isProductiveAuthor(authorId);

        // Then
        assertTrue(isProductive); // 3 books >= 3 threshold
    }

    @Test
    void shouldIdentifyNonProductiveAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        
        List<Book> authorBooks = Arrays.asList(book1, book2);
        
        when(bookService.getBooksForAuthor(authorId)).thenReturn(authorBooks);

        // When
        boolean isProductive = authorService.isProductiveAuthor(authorId);

        // Then
        assertFalse(isProductive); // 2 books < 3 threshold
    }

    @Test
    void shouldHandleProductivityBoundaryCase() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        Book book3 = new Book("Book 3", "ISBN3", 150);
        
        List<Book> exactlyThreeBooks = Arrays.asList(book1, book2, book3);
        
        when(bookService.getBooksForAuthor(authorId)).thenReturn(exactlyThreeBooks);

        // When
        boolean isProductive = authorService.isProductiveAuthor(authorId);

        // Then
        assertTrue(isProductive); // Exactly 3 books should be productive
    }

    @Test
    void shouldThrowExceptionForNullAuthorId() {
        // Configure mocks to throw exceptions for null input
        when(bookService.getBooksForAuthor(null))
            .thenThrow(new IllegalArgumentException("AuthorId is null"));
        when(bookService.getTotalPagesForAuthor(null))
            .thenThrow(new IllegalArgumentException("AuthorId is null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            authorService.getBookCountForAuthor(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            authorService.getTotalPagesWrittenByAuthor(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            authorService.isProductiveAuthor(null);
        });
    }
}
