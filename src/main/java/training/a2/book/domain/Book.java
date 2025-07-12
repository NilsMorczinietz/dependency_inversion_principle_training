package training.a2.book.domain;

import training.a2.author.domain.AuthorId;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private BookId id;
    private String title;
    private String isbn;
    private int pages;
    private List<AuthorId> authors;

    public Book() {
        this.authors = new ArrayList<>();
    }

    public Book(BookId id, String title, String isbn, int pages) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.pages = pages;
        this.authors = new ArrayList<>();
    }

    public BookId getId() {
        return id;
    }

    public void setId(BookId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<AuthorId> getAuthors() {
        return new ArrayList<>(authors);
    }

    public void addAuthor(training.a2.author.domain.Author author) {
        if (author != null && !authors.contains(author.getId())) {
            authors.add(author.getId());
        }
    }

    public void removeAuthor(AuthorId authorId) {
        authors.remove(authorId);
    }
}
