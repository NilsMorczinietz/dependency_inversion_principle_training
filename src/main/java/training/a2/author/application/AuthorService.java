package training.a2.author.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a2.author.domain.Author;
import training.a2.author.domain.AuthorId;
import training.a2.author.domain.AuthorRepository;
import training.a2.book.application.BookService;
import training.a2.book.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthorService {
    private AuthorRepository authorRepository;
    private BookService bookService;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    public Author addAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("Author is null");
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(AuthorId authorId) {
        if (authorId == null) throw new IllegalArgumentException("AuthorId is null");
        return authorRepository.findById(authorId);
    }

    public List<Author> getAuthorsByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        return authorRepository.findByLastName(lastName);
    }

    public List<Author> getAuthorsForBook(UUID bookId) {
        if (bookId == null) throw new IllegalArgumentException("BookId is null");
        return authorRepository.findByPublishedBookIdsContains(bookId);
    }

    public int getBookCountForAuthor(UUID authorId) {
        List<Book> books = bookService.getBooksForAuthor(authorId);
        return books.size();
    }

    public int getTotalPagesWrittenByAuthor(UUID authorId) {
        return bookService.getTotalPagesForAuthor(authorId);
    }

    public List<Author> searchAuthors(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        return authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            searchTerm, searchTerm);
    }

    public boolean isProductiveAuthor(UUID authorId) {
        return getBookCountForAuthor(authorId) >= 3;
    }
}
