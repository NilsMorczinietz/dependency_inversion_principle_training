package training.a2.book.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.a2.book.domain.Book;
import training.a2.book.domain.BookId;
import training.a2.book.domain.BookRepository;
import training.a2.author.domain.Author;
import training.a2.author.domain.AuthorId;
import training.a2.author.domain.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Book addBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book is null");
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(BookId bookId) {
        if (bookId == null) throw new IllegalArgumentException("BookId is null");
        return bookRepository.findById(bookId);
    }

    public List<Book> getBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBooksForAuthor(AuthorId authorId) {
        if (authorId == null) throw new IllegalArgumentException("AuthorId is null");
        return bookRepository.findByAuthorsContains(authorId);
    }

    public void assignAuthorToBook(Author author, Book book) {
        if (author == null) throw new IllegalArgumentException("Author is null");
        if (book == null) throw new IllegalArgumentException("Book is null");
        
        book.addAuthor(author);
        author.addBook(book);
        
        bookRepository.save(book);
        authorRepository.save(author);
    }

    public int getTotalPagesForAuthor(AuthorId authorId) {
        List<Book> books = getBooksForAuthor(authorId);
        return books.stream()
                   .mapToInt(Book::getPages)
                   .sum();
    }

    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        return bookRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
}
