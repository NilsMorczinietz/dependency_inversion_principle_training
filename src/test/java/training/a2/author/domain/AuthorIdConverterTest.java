package training.a2.author.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorIdConverterTest {

    private final AuthorIdConverter converter = new AuthorIdConverter();

    @Test
    void shouldConvertStringToAuthorId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        
        AuthorId result = converter.convert(uuidString);
        
        assertEquals(uuid, result.getId());
    }

    @Test
    void shouldHandleValidUuidString() {
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";
        
        AuthorId result = converter.convert(validUuid);
        
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
