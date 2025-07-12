package training.a3.product.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductIdConverter implements Converter<String, ProductId> {
    @Override
    public ProductId convert(String source) {
        return new ProductId(UUID.fromString(source));
    }
}
