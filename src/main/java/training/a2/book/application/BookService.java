package training.a2.book.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a2.book.domain.Book;
import training.a2.book.domain.BookId;
import training.a2.book.domain.BookRepository;
import training.a2.author.domain.Author;
import training.a2.author.domain.AuthorId;
import training.a2.author.domain.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
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

    public List<Book> getBooksForAuthor(UUID authorId) {
        if (authorId == null) throw new IllegalArgumentException("AuthorId is null");
        return bookRepository.findByAuthorIdsContains(authorId);
    }

    public void assignAuthorToBook(UUID authorId, UUID bookId) {
        if (authorId == null) throw new IllegalArgumentException("AuthorId is null");
        if (bookId == null) throw new IllegalArgumentException("BookId is null");
        
        Optional<Book> bookOpt = bookRepository.findById(new BookId(bookId));
        Optional<Author> authorOpt = authorRepository.findById(new AuthorId(authorId));
        
        if (bookOpt.isPresent() && authorOpt.isPresent()) {
            Book book = bookOpt.get();
            Author author = authorOpt.get();
            
            book.addAuthor(authorId);
            author.addBook(bookId);
            
            bookRepository.save(book);
            authorRepository.save(author);
        }
    }

    public int getTotalPagesForAuthor(UUID authorId) {
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
