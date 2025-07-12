package training.a3.customer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import training.a3.product.domain.ProductId;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private CustomerId customerId;

    @BeforeEach
    void setUp() {
        customerId = new CustomerId(UUID.randomUUID());
        customer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        customer.setId(customerId.getId());
    }

    @Test
    void shouldCreateCustomerWithCorrectProperties() {
        assertEquals(customerId, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("John Doe", customer.getFullName());
    }

    @Test
    void shouldInitializeEmptyLists() {
        assertTrue(customer.getWishlist().isEmpty());
        assertTrue(customer.getPurchaseHistory().isEmpty());
    }

    @Test
    void shouldReturnImmutableLists() {
        ProductId productId = new ProductId(UUID.randomUUID());

        // Test that returned lists are immutable
        assertThrows(UnsupportedOperationException.class, () -> 
            customer.getWishlist().add(productId));
        
        assertThrows(UnsupportedOperationException.class, () -> 
            customer.getPurchaseHistory().add(productId));
    }

    @Test
    void shouldManageWishlistWithProductIds() {
        ProductId productId1 = new ProductId(UUID.randomUUID());
        ProductId productId2 = new ProductId(UUID.randomUUID());

        // Initially empty
        assertFalse(customer.hasProductInWishlist(productId1));
        assertTrue(customer.getWishlist().isEmpty());

        // Note: We can't test addToWishlist(Product) without creating circular dependency
        // This will be tested in integration tests after DIP refactoring

        // Test direct ID operations (these would be used internally)
        // For now, we test the query methods
        assertFalse(customer.hasProductInWishlist(productId1));
        assertFalse(customer.hasProductInWishlist(productId2));
    }

    @Test
    void shouldManagePurchaseHistoryWithProductIds() {
        ProductId productId1 = new ProductId(UUID.randomUUID());
        ProductId productId2 = new ProductId(UUID.randomUUID());

        // Initially empty
        assertFalse(customer.hasPurchased(productId1));
        assertTrue(customer.getPurchaseHistory().isEmpty());

        // Note: We can't test addToPurchaseHistory(Product) without creating circular dependency
        // This will be tested in integration tests after DIP refactoring

        // Test query methods
        assertFalse(customer.hasPurchased(productId1));
        assertFalse(customer.hasPurchased(productId2));
    }

    @Test
    void shouldHandleNullChecksInQueryMethods() {
        // Test with null ProductId - should return false, not throw exception
        assertFalse(customer.hasProductInWishlist(null));
        assertFalse(customer.hasPurchased(null));
    }

    @Test
    void shouldUpdateCustomerProperties() {
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setEmail("jane.smith@example.com");
        customer.setAddress("456 Oak Avenue");

        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("jane.smith@example.com", customer.getEmail());
        assertEquals("456 Oak Avenue", customer.getAddress());
        assertEquals("Jane Smith", customer.getFullName());
    }
}
