package training.a2.book.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookIdConverter implements Converter<String, BookId> {
    @Override
    public BookId convert(String source) {
        return new BookId(UUID.fromString(source));
    }
}
