package training.a3.customer.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerIdConverterTest {

    private final CustomerIdConverter converter = new CustomerIdConverter();

    @Test
    void shouldConvertStringToCustomerId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        
        CustomerId result = converter.convert(uuidString);
        
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
