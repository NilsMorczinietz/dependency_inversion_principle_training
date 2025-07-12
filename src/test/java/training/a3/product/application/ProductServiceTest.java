package training.a3.product.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a3.customer.application.CustomerService;
import training.a3.customer.domain.Customer;
import training.a3.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    private Customer customer1, customer2;
    private Product product1, product2, product3;

    @BeforeEach
    void setUp() {
        initializeCustomers();
        initializeProducts();
        // Setup interests will be done in individual tests to avoid session issues
    }

    private void initializeCustomers() {
        customer1 = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        customer1 = customerService.addCustomer(customer1);
        
        customer2 = new Customer("Jane", "Smith", "jane.smith@example.com", "456 Oak Ave");
        customer2 = customerService.addCustomer(customer2);
    }

    private void initializeProducts() {
        product1 = new Product("Laptop", "High-performance laptop", new BigDecimal("999.99"), 10, "Electronics");
        product1 = productService.addProduct(product1);
        
        product2 = new Product("Book", "Programming guide", new BigDecimal("49.99"), 50, "Books");
        product2 = productService.addProduct(product2);
        
        product3 = new Product("Headphones", "Noise-cancelling headphones", new BigDecimal("199.99"), 25, "Electronics");
        product3 = productService.addProduct(product3);
    }

    @Test
    void shouldReturnCorrectTotalWishlistValue() {
        // Given - setup customer interests
        productService.addCustomerInterest(customer1.getId().getId(), product1.getId().getId());
        productService.addCustomerInterest(customer1.getId().getId(), product2.getId().getId());
        
        UUID customerId = customer1.getId().getId();
        BigDecimal expectedTotal = new BigDecimal("1049.98");
        
        // When
        BigDecimal actualTotal = productService.getTotalWishlistValue(customerId);
        
        // Then
        assertEquals(0, expectedTotal.compareTo(actualTotal));
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistProducts() {
        // Given - new customer with no wishlist products
        Customer newCustomer = new Customer("Bob", "Wilson", "bob@example.com", "789 Pine St");
        newCustomer = customerService.addCustomer(newCustomer);
        
        // When
        BigDecimal totalValue = productService.getTotalWishlistValue(newCustomer.getId().getId());
        
        // Then
        assertEquals(BigDecimal.ZERO, totalValue);
    }

    @Test
    void shouldReturnCorrectProductsForCustomer() {
        // Given - setup customer interests
        productService.addCustomerInterest(customer1.getId().getId(), product1.getId().getId());
        productService.addCustomerInterest(customer1.getId().getId(), product2.getId().getId());
        
        UUID customerId = customer1.getId().getId();
        
        // When
        List<Product> products = productService.getProductsForCustomer(customerId);
        
        // Then
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Book")));
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoProducts() {
        // Given - new customer with no products
        Customer newCustomer = new Customer("Alice", "Brown", "alice@example.com", "321 Elm St");
        newCustomer = customerService.addCustomer(newCustomer);
        
        // When
        List<Product> products = productService.getProductsForCustomer(newCustomer.getId().getId());
        
        // Then
        assertTrue(products.isEmpty());
    }

    @Test
    void shouldSuccessfullyPurchaseProductWithSufficientStock() {
        // Given
        UUID customerId = customer1.getId().getId();
        UUID productId = product1.getId().getId();
        int quantity = 2;
        int initialStock = product1.getStockQuantity();
        
        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);
        
        // Then
        assertTrue(success);
        // Reload product to check stock
        Product updatedProduct = productService.getProductById(product1.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(initialStock - quantity, updatedProduct.getStockQuantity());
    }

    @Test
    void shouldFailToPurchaseProductWithInsufficientStock() {
        // Given
        UUID customerId = customer1.getId().getId();
        UUID productId = product1.getId().getId();
        int quantity = 15; // More than available stock (10)
        int initialStock = product1.getStockQuantity();
        
        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);
        
        // Then
        assertFalse(success);
        // Stock should remain unchanged
        Product unchangedProduct = productService.getProductById(product1.getId()).orElse(null);
        assertNotNull(unchangedProduct);
        assertEquals(initialStock, unchangedProduct.getStockQuantity());
    }

    @Test
    void shouldFailToPurchaseProductWhenCustomerNotFound() {
        // Given
        UUID nonExistentCustomerId = UUID.randomUUID();
        UUID productId = product1.getId().getId();
        int quantity = 2;
        
        // When
        boolean success = productService.purchaseProduct(nonExistentCustomerId, productId, quantity);
        
        // Then
        assertFalse(success);
    }

    @Test
    void shouldFailToPurchaseProductWhenProductNotFound() {
        // Given
        UUID customerId = customer1.getId().getId();
        UUID nonExistentProductId = UUID.randomUUID();
        int quantity = 2;
        
        // When
        boolean success = productService.purchaseProduct(customerId, nonExistentProductId, quantity);
        
        // Then
        assertFalse(success);
    }

    @Test
    void shouldThrowExceptionForNullParameters() {
        // When & Then - getProductsForCustomer
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsForCustomer(null);
        });

        // When & Then - getTotalWishlistValue
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getTotalWishlistValue(null);
        });

        // When & Then - purchaseProduct with null customerId
        assertThrows(IllegalArgumentException.class, () -> {
            productService.purchaseProduct(null, UUID.randomUUID(), 1);
        });

        // When & Then - purchaseProduct with null productId
        assertThrows(IllegalArgumentException.class, () -> {
            productService.purchaseProduct(UUID.randomUUID(), null, 1);
        });

        // When & Then - addCustomerInterest with null customerId
        assertThrows(IllegalArgumentException.class, () -> {
            productService.addCustomerInterest(null, UUID.randomUUID());
        });

        // When & Then - addCustomerInterest with null productId
        assertThrows(IllegalArgumentException.class, () -> {
            productService.addCustomerInterest(UUID.randomUUID(), null);
        });

        // When & Then - addProduct with null
        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(null);
        });

        // When & Then - getProductById with null
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(null);
        });
    }

    @Test
    void shouldThrowExceptionForInvalidQuantity() {
        // When & Then - zero quantity
        assertThrows(IllegalArgumentException.class, () -> {
            productService.purchaseProduct(UUID.randomUUID(), UUID.randomUUID(), 0);
        });

        // When & Then - negative quantity
        assertThrows(IllegalArgumentException.class, () -> {
            productService.purchaseProduct(UUID.randomUUID(), UUID.randomUUID(), -1);
        });
    }

    @Test
    void shouldThrowExceptionForNullOrEmptySearchTerm() {
        // When & Then - null search term
        assertThrows(IllegalArgumentException.class, () -> {
            productService.searchProducts(null);
        });

        // When & Then - empty search term
        assertThrows(IllegalArgumentException.class, () -> {
            productService.searchProducts("");
        });

        // When & Then - whitespace only search term
        assertThrows(IllegalArgumentException.class, () -> {
            productService.searchProducts("   ");
        });
    }

    @Test
    void shouldThrowExceptionForNullOrEmptyCategory() {
        // When & Then - null category
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByCategory(null);
        });

        // When & Then - empty category
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByCategory("");
        });

        // When & Then - whitespace only category
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByCategory("   ");
        });
    }
}
