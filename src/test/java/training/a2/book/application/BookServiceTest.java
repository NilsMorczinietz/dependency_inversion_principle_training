package training.a2.book.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import training.a2.book.domain.Book;
import training.a2.book.domain.BookRepository;
import training.a2.author.domain.AuthorRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, authorRepository);
    }

    @Test
    void shouldReturnBooksForAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        Book book3 = new Book("Book 3", "ISBN3", 150);
        
        List<Book> expectedBooks = Arrays.asList(book1, book2, book3);
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(expectedBooks);

        // When
        List<Book> actualBooks = bookService.getBooksForAuthor(authorId);

        // Then
        assertEquals(3, actualBooks.size());
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void shouldReturnEmptyListWhenAuthorHasNoBooks() {
        // Given
        UUID authorId = UUID.randomUUID();
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(Collections.emptyList());

        // When
        List<Book> actualBooks = bookService.getBooksForAuthor(authorId);

        // Then
        assertTrue(actualBooks.isEmpty());
    }

    @Test
    void shouldCalculateTotalPagesForAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 200);
        Book book2 = new Book("Book 2", "ISBN2", 300);
        Book book3 = new Book("Book 3", "ISBN3", 150);
        
        List<Book> authorBooks = Arrays.asList(book1, book2, book3);
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(authorBooks);

        // When
        int totalPages = bookService.getTotalPagesForAuthor(authorId);

        // Then
        assertEquals(650, totalPages); // 200 + 300 + 150
    }

    @Test
    void shouldReturnZeroWhenAuthorHasNoBooksForPagesCalculation() {
        // Given
        UUID authorId = UUID.randomUUID();
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(Collections.emptyList());

        // When
        int totalPages = bookService.getTotalPagesForAuthor(authorId);

        // Then
        assertEquals(0, totalPages);
    }

    @Test
    void shouldHandleSingleBookForAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book singleBook = new Book("Single Book", "ISBN1", 425);
        
        List<Book> singleBookList = Arrays.asList(singleBook);
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(singleBookList);

        // When
        List<Book> actualBooks = bookService.getBooksForAuthor(authorId);
        int totalPages = bookService.getTotalPagesForAuthor(authorId);

        // Then
        assertEquals(1, actualBooks.size());
        assertEquals(425, totalPages);
        assertEquals("Single Book", actualBooks.get(0).getTitle());
    }

    @Test
    void shouldHandleBooksWithZeroPages() {
        // Given
        UUID authorId = UUID.randomUUID();
        Book book1 = new Book("Book 1", "ISBN1", 0);
        Book book2 = new Book("Book 2", "ISBN2", 100);
        
        List<Book> authorBooks = Arrays.asList(book1, book2);
        
        when(bookRepository.findByAuthorIdsContains(authorId)).thenReturn(authorBooks);

        // When
        int totalPages = bookService.getTotalPagesForAuthor(authorId);

        // Then
        assertEquals(100, totalPages); // 0 + 100
    }

    @Test
    void shouldThrowExceptionForNullAuthorId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBooksForAuthor(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getTotalPagesForAuthor(null);
        });
    }
}
