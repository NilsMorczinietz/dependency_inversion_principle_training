package training.a2.author.domain;

import training.GenericId;

import java.util.UUID;

public class AuthorId extends GenericId {
    public AuthorId() {
        super();
    }
    
    public AuthorId(UUID id) {
        super(id);
    }
}
