package training.a2.author.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a2.book.domain.BookId;

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
    
    public void setId(UUID id){
        this.id = new AuthorId(id);
    }
    
    private String firstName;
    private String lastName;
    private String email;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<BookId> publishedBooks;

    public Author(String firstName, String lastName, String email) {
        this.id = new AuthorId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.publishedBooks = new ArrayList<>();
    }

    public AuthorId getId() {
        return id;
    }

    public void setId(AuthorId id) {
        this.id = id;
    }

    // Override Lombok getters for collections to return defensive copies
    public List<BookId> getPublishedBooks() {
        return List.copyOf(publishedBooks);
    }
    
    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addBook(training.a2.book.domain.Book book) {
        if (book != null && !publishedBooks.contains(book.getId())) {
            publishedBooks.add(book.getId());
        }
    }

    public void removeBook(BookId bookId) {
        publishedBooks.remove(bookId);
    }

    public int getBookCount() {
        return publishedBooks.size();
    }
}
