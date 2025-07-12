package training.a2.book.domain;

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
public class Book {
    
    @Getter
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private BookId id;
    
    private String title;
    private String isbn;
    private int pages;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> authorIds;

    public Book(String title, String isbn, int pages) {
        this.id = new BookId();
        this.title = title;
        this.isbn = isbn;
        this.pages = pages;
        this.authorIds = new ArrayList<>();
    }

    // Override Lombok getters for collections to return defensive copies
    public List<UUID> getAuthorIds() {
        return List.copyOf(authorIds);
    }
    
    // Business methods
    public void addAuthor(UUID authorId) {
        if (authorId != null && !authorIds.contains(authorId)) {
            authorIds.add(authorId);
        }
    }

    public void removeAuthor(UUID authorId) {
        authorIds.remove(authorId);
    }

    public int getAuthorCount() {
        return authorIds.size();
    }
}