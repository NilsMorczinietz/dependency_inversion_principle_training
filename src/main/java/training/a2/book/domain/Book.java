package training.a2.book.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a2.author.domain.AuthorId;

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
    
    public void setId(UUID id){
        this.id = new BookId(id);
    }
    
    private String title;
    private String isbn;
    private int pages;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<AuthorId> authors;

    public Book(String title, String isbn, int pages) {
        this.id = new BookId();
        this.title = title;
        this.isbn = isbn;
        this.pages = pages;
        this.authors = new ArrayList<>();
    }

    public void setId(BookId id) {
        this.id = id;
    }

    // Override Lombok getters for collections to return defensive copies
    public List<AuthorId> getAuthors() {
        return List.copyOf(authors);
    }
    
    // Business methods
    public void addAuthor(training.a2.author.domain.Author author) {
        if (author != null && !authors.contains(author.getId())) {
            authors.add(author.getId());
        }
    }

    public void removeAuthor(AuthorId authorId) {
        authors.remove(authorId);
    }

    public int getAuthorCount() {
        return authors.size();
    }
}