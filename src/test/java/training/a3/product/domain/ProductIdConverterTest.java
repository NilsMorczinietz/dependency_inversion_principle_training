package training.a3.product.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdConverterTest {

    private final ProductIdConverter converter = new ProductIdConverter();

    @Test
    void shouldConvertStringToProductId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        
        ProductId result = converter.convert(uuidString);
        
        assertNotNull(result);
        assertEquals(uuid, result.getId());
    }

    @Test
    void shouldThrowExceptionForInvalidUUIDString() {
        String invalidUuid = "not-a-valid-uuid";
        
        assertThrows(IllegalArgumentException.class, () -> 
            converter.convert(invalidUuid));
    }

    @Test
    void shouldHandleNullInput() {
        assertThrows(NullPointerException.class, () -> 
            converter.convert(null));
    }
}
