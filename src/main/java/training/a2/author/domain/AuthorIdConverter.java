package training.a2.author.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthorIdConverter implements Converter<String, AuthorId> {
    @Override
    public AuthorId convert(String source) {
        return new AuthorId(UUID.fromString(source));
    }
}
