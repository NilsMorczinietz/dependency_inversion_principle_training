package training.a2.author.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Author {
    
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private AuthorId id;
    
    private String firstName;
    private String lastName;
    private String email;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> publishedBookIds;

    public Author(String firstName, String lastName, String email) {
        this.id = new AuthorId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.publishedBookIds = new ArrayList<>();
    }

    // Override Lombok getters for collections to return defensive copies
    public List<UUID> getPublishedBookIds() {
        return List.copyOf(publishedBookIds);
    }
    
    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addBook(UUID bookId) {
        if (bookId != null && !publishedBookIds.contains(bookId)) {
            publishedBookIds.add(bookId);
        }
    }

    public void removeBook(UUID bookId) {
        publishedBookIds.remove(bookId);
    }

    public int getBookCount() {
        return publishedBookIds.size();
    }
}
