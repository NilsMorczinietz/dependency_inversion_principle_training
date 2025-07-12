package training.a3.customer.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerIdConverter implements Converter<String, CustomerId> {
    @Override
    public CustomerId convert(String source) {
        return new CustomerId(UUID.fromString(source));
    }
}
