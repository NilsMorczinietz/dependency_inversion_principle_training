package training.a2.book.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookIdConverterTest {

    private final BookIdConverter converter = new BookIdConverter();

    @Test
    void shouldConvertStringToBookId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        
        BookId result = converter.convert(uuidString);
        
        assertEquals(uuid, result.getId());
    }

    @Test
    void shouldHandleValidUuidString() {
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";
        
        BookId result = converter.convert(validUuid);
        
        assertNotNull(result);
        assertEquals(UUID.fromString(validUuid), result.getId());
    }

    @Test
    void shouldThrowExceptionForInvalidUuidString() {
        String invalidUuid = "not-a-valid-uuid";
        
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convert(invalidUuid);
        });
    }
}
