package training.a2.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, BookId> {
    List<Book> findByTitle(String title);
    
    List<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE :authorId MEMBER OF b.authorIds")
    List<Book> findByAuthorIdsContains(UUID authorId);
    
    List<Book> findByTitleContainingIgnoreCase(String titlePart);
}
