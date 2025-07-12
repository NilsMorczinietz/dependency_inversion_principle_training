package training.a3.product.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void shouldCreateProductIdWithRandomUUID() {
        ProductId productId = new ProductId();
        assertNotNull(productId.getId());
    }

    @Test
    void shouldCreateProductIdWithSpecificUUID() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = new ProductId(uuid);
        assertEquals(uuid, productId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        UUID uuid = UUID.randomUUID();
        ProductId productId1 = new ProductId(uuid);
        ProductId productId2 = new ProductId(uuid);
        ProductId productId3 = new ProductId();

        assertEquals(productId1, productId2);
        assertEquals(productId1.hashCode(), productId2.hashCode());
        assertNotEquals(productId1, productId3);
    }

    @Test
    void shouldHaveStringRepresentation() {
        ProductId productId = new ProductId();
        String stringRepresentation = productId.toString();
        assertNotNull(stringRepresentation);
        assertFalse(stringRepresentation.isEmpty());
    }
}
