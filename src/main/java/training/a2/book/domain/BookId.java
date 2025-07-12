package training.a2.book.domain;

import training.GenericId;

import java.util.UUID;

public class BookId extends GenericId {
    public BookId() {
        super();
    }
    
    public BookId(UUID id) {
        super(id);
    }
}
