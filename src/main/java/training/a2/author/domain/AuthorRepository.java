package training.a2.author.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, AuthorId> {
    List<Author> findByFirstName(String firstName);
    
    List<Author> findByLastName(String lastName);
    
    List<Author> findByEmail(String email);
    
    @Query("SELECT a FROM Author a WHERE :bookId MEMBER OF a.publishedBookIds")
    List<Author> findByPublishedBookIdsContains(UUID bookId);
    
    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstNamePart, String lastNamePart);
}
