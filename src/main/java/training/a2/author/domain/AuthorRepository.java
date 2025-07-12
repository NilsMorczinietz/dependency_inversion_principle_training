package training.a2.author.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import training.a2.book.domain.BookId;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, AuthorId> {
    List<Author> findByFirstName(String firstName);
    
    List<Author> findByLastName(String lastName);
    
    List<Author> findByEmail(String email);
    
    @Query("SELECT a FROM Author a WHERE :bookId MEMBER OF a.publishedBooks")
    List<Author> findByPublishedBooksContains(BookId bookId);
    
    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstNamePart, String lastNamePart);
}
