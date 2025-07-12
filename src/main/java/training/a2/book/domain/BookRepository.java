package training.a2.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import training.a2.author.domain.AuthorId;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, BookId> {
    List<Book> findByTitle(String title);
    
    List<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE :authorId MEMBER OF b.authors")
    List<Book> findByAuthorsContains(AuthorId authorId);
    
    List<Book> findByTitleContainingIgnoreCase(String titlePart);
}
