package training.a3.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import training.a3.customer.domain.CustomerId;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private ProductId productId;

    @BeforeEach
    void setUp() {
        productId = new ProductId(UUID.randomUUID());
        product = new Product("Test Product", "A test product", 
                            new BigDecimal("29.99"), 10, "Electronics");
        product.setId(productId.getId());
    }

    @Test
    void shouldCreateProductWithCorrectProperties() {
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("A test product", product.getDescription());
        assertEquals(new BigDecimal("29.99"), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertEquals("Electronics", product.getCategory());
        assertTrue(product.isInStock());
    }

    @Test
    void shouldManageInterestedCustomers() {
        CustomerId customerId1 = new CustomerId(UUID.randomUUID());

        assertTrue(product.getInterestedCustomers().isEmpty());

        // Note: We can't test addInterestedCustomer(Customer) without creating circular dependency
        // This will be tested in integration tests after DIP refactoring

        // Test that the list is initially empty and immutable
        assertThrows(UnsupportedOperationException.class, () -> 
            product.getInterestedCustomers().add(customerId1));
    }

    @Test
    void shouldReduceStockCorrectly() {
        assertEquals(10, product.getStockQuantity());
        assertTrue(product.isInStock());

        product.reduceStock(3);
        assertEquals(7, product.getStockQuantity());
        assertTrue(product.isInStock());

        product.reduceStock(7);
        assertEquals(0, product.getStockQuantity());
        assertFalse(product.isInStock());
    }

    @Test
    void shouldThrowExceptionWhenReducingStockBeyondAvailable() {
        assertEquals(10, product.getStockQuantity());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            product.reduceStock(15));
        
        assertEquals("Not enough stock available", exception.getMessage());
        assertEquals(10, product.getStockQuantity()); // Stock should remain unchanged
    }

    @Test
    void shouldHandleZeroStock() {
        product.setStockQuantity(0);
        assertFalse(product.isInStock());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            product.reduceStock(1));
        
        assertEquals("Not enough stock available", exception.getMessage());
    }

    @Test
    void shouldAllowStockReplenishment() {
        product.reduceStock(10);
        assertEquals(0, product.getStockQuantity());
        assertFalse(product.isInStock());

        product.setStockQuantity(5);
        assertEquals(5, product.getStockQuantity());
        assertTrue(product.isInStock());
    }
}
