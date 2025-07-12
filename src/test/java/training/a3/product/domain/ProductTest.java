package training.a3.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Test Product", "A test product", 
                            new BigDecimal("29.99"), 10, "Electronics");
    }

    @Test
    void shouldCreateProductWithCorrectProperties() {
        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("A test product", product.getDescription());
        assertEquals(new BigDecimal("29.99"), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertEquals("Electronics", product.getCategory());
        assertTrue(product.isInStock());
    }

    @Test
    void shouldManageInterestedCustomers() {
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();

        assertTrue(product.getInterestedCustomerIds().isEmpty());

        // Add interested customer
        product.addInterestedCustomer(customerId1);
        assertEquals(1, product.getInterestedCustomerIds().size());
        assertTrue(product.getInterestedCustomerIds().contains(customerId1));

        // Add another customer
        product.addInterestedCustomer(customerId2);
        assertEquals(2, product.getInterestedCustomerIds().size());
        assertTrue(product.getInterestedCustomerIds().contains(customerId2));

        // Remove customer
        product.removeInterestedCustomer(customerId1);
        assertEquals(1, product.getInterestedCustomerIds().size());
        assertFalse(product.getInterestedCustomerIds().contains(customerId1));
        assertTrue(product.getInterestedCustomerIds().contains(customerId2));
    }

    @Test
    void shouldReturnImmutableCustomersList() {
        UUID customerId = UUID.randomUUID();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            product.getInterestedCustomerIds().add(customerId);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            product.getInterestedCustomerIds().clear();
        });
    }

    @Test
    void shouldManageStockCorrectly() {
        assertEquals(10, product.getStockQuantity());
        assertTrue(product.isInStock());

        // Reduce stock
        product.reduceStock(5);
        assertEquals(5, product.getStockQuantity());
        assertTrue(product.isInStock());

        // Reduce to zero
        product.reduceStock(5);
        assertEquals(0, product.getStockQuantity());
        assertFalse(product.isInStock());
    }

    @Test
    void shouldThrowExceptionForInsufficientStock() {
        product.reduceStock(5); // Stock becomes 5
        
        assertThrows(IllegalArgumentException.class, () -> {
            product.reduceStock(6); // Try to reduce more than available
        });
    }

    @Test
    void shouldHandlePropertyChanges() {
        product.setName("Updated Product");
        product.setDescription("Updated description");
        product.setPrice(new BigDecimal("39.99"));
        product.setCategory("Updated Category");
        
        assertEquals("Updated Product", product.getName());
        assertEquals("Updated description", product.getDescription());
        assertEquals(new BigDecimal("39.99"), product.getPrice());
        assertEquals("Updated Category", product.getCategory());
    }

    @Test
    void shouldHandleDuplicateCustomerInterest() {
        UUID customerId = UUID.randomUUID();
        
        product.addInterestedCustomer(customerId);
        assertEquals(1, product.getInterestedCustomerIds().size());
        
        // Adding same customer again should not increase size
        product.addInterestedCustomer(customerId);
        assertEquals(1, product.getInterestedCustomerIds().size());
    }

    @Test
    void shouldHandleNullCustomerIds() {
        // Adding null should not crash
        product.addInterestedCustomer(null);
        assertEquals(0, product.getInterestedCustomerIds().size());
        
        // Removing null should not crash
        product.removeInterestedCustomer(null);
        assertEquals(0, product.getInterestedCustomerIds().size());
    }
}