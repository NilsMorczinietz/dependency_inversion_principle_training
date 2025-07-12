package training.a3.customer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
    }

    @Test
    void shouldCreateCustomerWithCorrectProperties() {
        assertNotNull(customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("John Doe", customer.getFullName());
    }

    @Test
    void shouldInitializeEmptyLists() {
        assertTrue(customer.getProductWishlistIds().isEmpty());
        assertTrue(customer.getPurchaseHistoryIds().isEmpty());
    }

    @Test
    void shouldReturnImmutableLists() {
        UUID productId = UUID.randomUUID();
        
        // Test that lists are immutable
        assertThrows(UnsupportedOperationException.class, () ->
            customer.getProductWishlistIds().add(productId));
        
        assertThrows(UnsupportedOperationException.class, () ->
            customer.getPurchaseHistoryIds().add(productId));
    }

    @Test
    void shouldManageWishlist() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        
        // Initially empty
        assertEquals(0, customer.getWishlistSize());
        assertTrue(customer.getProductWishlistIds().isEmpty());
        
        // Add to wishlist
        customer.addToWishlist(productId1);
        assertEquals(1, customer.getWishlistSize());
        assertTrue(customer.hasProductInWishlist(productId1));
        assertFalse(customer.hasProductInWishlist(productId2));
        
        // Add another
        customer.addToWishlist(productId2);
        assertEquals(2, customer.getWishlistSize());
        assertTrue(customer.hasProductInWishlist(productId2));
        
        // Remove from wishlist
        customer.removeFromWishlist(productId1);
        assertEquals(1, customer.getWishlistSize());
        assertFalse(customer.hasProductInWishlist(productId1));
        assertTrue(customer.hasProductInWishlist(productId2));
    }

    @Test
    void shouldManagePurchaseHistory() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        
        // Initially empty
        assertEquals(0, customer.getPurchaseCount());
        assertTrue(customer.getPurchaseHistoryIds().isEmpty());
        
        // Add purchase
        customer.addToPurchaseHistory(productId1);
        assertEquals(1, customer.getPurchaseCount());
        assertTrue(customer.hasPurchased(productId1));
        assertFalse(customer.hasPurchased(productId2));
        
        // Add another purchase
        customer.addToPurchaseHistory(productId2);
        assertEquals(2, customer.getPurchaseCount());
        assertTrue(customer.hasPurchased(productId2));
    }

    @Test
    void shouldHandlePropertyChanges() {
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setEmail("jane.smith@example.com");
        customer.setAddress("456 Oak St");
        
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("jane.smith@example.com", customer.getEmail());
        assertEquals("456 Oak St", customer.getAddress());
        assertEquals("Jane Smith", customer.getFullName());
    }

    @Test
    void shouldHandleDuplicateWishlistEntries() {
        UUID productId = UUID.randomUUID();
        
        customer.addToWishlist(productId);
        assertEquals(1, customer.getWishlistSize());
        
        // Adding same product again should not increase size
        customer.addToWishlist(productId);
        assertEquals(1, customer.getWishlistSize());
    }

    @Test
    void shouldHandleNullValues() {
        // Adding null should not crash
        customer.addToWishlist(null);
        assertEquals(0, customer.getWishlistSize());
        
        customer.addToPurchaseHistory(null);
        assertEquals(0, customer.getPurchaseCount());
    }
}
