package training.a2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a2.author.application.AuthorService;
import training.a2.author.domain.Author;
import training.a2.book.application.BookService;
import training.a2.book.domain.Book;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class AuthorBookTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    private Author author1, author2, author3;
    private Book book1, book2, book3, book4, book5;

    @BeforeEach
    public void setUp() {
        initializeAuthors();
        initializeBooks();
        assignAuthorsToBooks();
    }

    /**
     * Initializes the authors used in the tests.
     */
    private void initializeAuthors() {
        author1 = new Author("Stephen", "King", "stephen.king@example.com");
        author1 = authorService.addAuthor(author1);
        
        author2 = new Author("J.K.", "Rowling", "jk.rowling@example.com");
        author2 = authorService.addAuthor(author2);
        
        author3 = new Author("George", "Orwell", "george.orwell@example.com");
        author3 = authorService.addAuthor(author3);
    }

    /**
     * Initializes the books used in the tests.
     */
    private void initializeBooks() {
        book1 = new Book("The Shining", "978-0-385-12167-5", 447);
        book1 = bookService.addBook(book1);
        
        book2 = new Book("It", "978-0-670-81302-9", 1138);
        book2 = bookService.addBook(book2);
        
        book3 = new Book("Harry Potter and the Philosopher's Stone", "978-0-7475-3269-9", 223);
        book3 = bookService.addBook(book3);
        
        book4 = new Book("1984", "978-0-452-28423-4", 328);
        book4 = bookService.addBook(book4);
        
        book5 = new Book("Carrie", "978-0-385-08695-0", 199);
        book5 = bookService.addBook(book5);
    }

    /**
     * Assigns authors to books to establish relationships.
     */
    private void assignAuthorsToBooks() {
        // Stephen King writes The Shining, It, and Carrie (3 books - productive)
        bookService.assignAuthorToBook(author1.getId().getId(), book1.getId().getId());
        bookService.assignAuthorToBook(author1.getId().getId(), book2.getId().getId());
        bookService.assignAuthorToBook(author1.getId().getId(), book5.getId().getId());
        
        // J.K. Rowling writes Harry Potter
        bookService.assignAuthorToBook(author2.getId().getId(), book3.getId().getId());
        
        // George Orwell writes 1984
        bookService.assignAuthorToBook(author3.getId().getId(), book4.getId().getId());
    }

    @Test
    public void testBookCountForAuthors() {
        // given, when, then
        assertEquals(3, authorService.getBookCountForAuthor(author1.getId().getId()));
        assertEquals(1, authorService.getBookCountForAuthor(author2.getId().getId()));
        assertEquals(1, authorService.getBookCountForAuthor(author3.getId().getId()));
    }

    @Test
    public void testTotalPagesWrittenByAuthors() {
        // Stephen King: The Shining (447) + It (1138) + Carrie (199) = 1784 pages
        assertEquals(1784, authorService.getTotalPagesWrittenByAuthor(author1.getId().getId()));
        
        // J.K. Rowling: Harry Potter (223) = 223 pages
        assertEquals(223, authorService.getTotalPagesWrittenByAuthor(author2.getId().getId()));
        
        // George Orwell: 1984 (328) = 328 pages
        assertEquals(328, authorService.getTotalPagesWrittenByAuthor(author3.getId().getId()));
    }

    @Test
    public void testProductiveAuthorIdentification() {
        // Stephen King with 2 books should be productive (>= 2 books)
        assertTrue(authorService.isProductiveAuthor(author1.getId().getId()));
        
        // J.K. Rowling with 1 book should not be productive (< 2 books)
        assertFalse(authorService.isProductiveAuthor(author2.getId().getId()));
        
        // George Orwell with 1 book should not be productive (< 2 books)
        assertFalse(authorService.isProductiveAuthor(author3.getId().getId()));
    }

    @Test
    public void testBooksForSpecificAuthor() {
        // Test that we get the correct books for Stephen King
        var stephenKingBooks = bookService.getBooksForAuthor(author1.getId().getId());
        assertEquals(3, stephenKingBooks.size());
        assertTrue(stephenKingBooks.stream().anyMatch(book -> book.getTitle().equals("The Shining")));
        assertTrue(stephenKingBooks.stream().anyMatch(book -> book.getTitle().equals("It")));
        assertTrue(stephenKingBooks.stream().anyMatch(book -> book.getTitle().equals("Carrie")));
        
        // Test that we get the correct book for J.K. Rowling
        var jkRowlingBooks = bookService.getBooksForAuthor(author2.getId().getId());
        assertEquals(1, jkRowlingBooks.size());
        assertEquals("Harry Potter and the Philosopher's Stone", jkRowlingBooks.get(0).getTitle());
    }

    @Test
    public void testTotalPagesForSpecificAuthor() {
        // Test BookService method for total pages
        assertEquals(1784, bookService.getTotalPagesForAuthor(author1.getId().getId()));
        assertEquals(223, bookService.getTotalPagesForAuthor(author2.getId().getId()));
        assertEquals(328, bookService.getTotalPagesForAuthor(author3.getId().getId()));
    }
}
