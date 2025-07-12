package training.a2.author.domain;

import training.a2.book.domain.BookId;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private AuthorId id;
    private String firstName;
    private String lastName;
    private String email;
    private List<BookId> publishedBooks;

    public Author() {
        this.publishedBooks = new ArrayList<>();
    }

    public Author(AuthorId id, String firstName, String lastName, String email) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<BookId> getPublishedBooks() {
        return new ArrayList<>(publishedBooks);
    }

    public void addBook(training.a2.book.domain.Book book) {
        if (book != null && !publishedBooks.contains(book.getId())) {
            publishedBooks.add(book.getId());
        }
    }

    public void removeBook(BookId bookId) {
        publishedBooks.remove(bookId);
    }
}
