package training.a3.customer.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerIdTest {

    @Test
    void shouldCreateCustomerIdWithRandomUUID() {
        CustomerId customerId = new CustomerId();
        assertNotNull(customerId.getId());
    }

    @Test
    void shouldCreateCustomerIdWithSpecificUUID() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(uuid);
        assertEquals(uuid, customerId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId1 = new CustomerId(uuid);
        CustomerId customerId2 = new CustomerId(uuid);
        CustomerId customerId3 = new CustomerId();

        assertEquals(customerId1, customerId2);
        assertEquals(customerId1.hashCode(), customerId2.hashCode());
        assertNotEquals(customerId1, customerId3);
    }

    @Test
    void shouldHaveStringRepresentation() {
        CustomerId customerId = new CustomerId();
        String stringRepresentation = customerId.toString();
        assertNotNull(stringRepresentation);
        assertFalse(stringRepresentation.isEmpty());
    }
}
