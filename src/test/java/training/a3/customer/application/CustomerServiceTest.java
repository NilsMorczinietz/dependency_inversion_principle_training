package training.a3.customer.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a3.customer.domain.Customer;
import training.a3.product.application.ProductService;
import training.a3.product.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    private Customer customer1, customer2;
    private Product product1, product2, product3;

    @BeforeEach
    void setUp() {
        initializeCustomers();
        initializeProducts();
        // Setup wishlists will be done in individual tests to avoid session issues
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
    void shouldReturnCorrectWishlistSize() {
        // Given - setup wishlists
        productService.addCustomerInterest(customer1.getId().getId(), product1.getId().getId());
        productService.addCustomerInterest(customer1.getId().getId(), product2.getId().getId());
        productService.addCustomerInterest(customer2.getId().getId(), product3.getId().getId());
        
        // When, Then
        assertEquals(2, customerService.getWishlistSize(customer1.getId().getId()));
        assertEquals(1, customerService.getWishlistSize(customer2.getId().getId()));
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistItems() {
        // Given - new customer with no wishlist items
        Customer newCustomer = new Customer("Bob", "Wilson", "bob@example.com", "789 Pine St");
        newCustomer = customerService.addCustomer(newCustomer);
        
        // When, Then
        assertEquals(0, customerService.getWishlistSize(newCustomer.getId().getId()));
    }

    @Test
    void shouldReturnCorrectWishlistTotalValue() {
        // Given - setup wishlist for customer1 with laptop (999.99) + book (49.99) = 1049.98
        productService.addCustomerInterest(customer1.getId().getId(), product1.getId().getId());
        productService.addCustomerInterest(customer1.getId().getId(), product2.getId().getId());
        
        BigDecimal expectedTotal = new BigDecimal("1049.98");
        
        // When
        BigDecimal actualTotal = customerService.getWishlistTotalValue(customer1.getId().getId());
        
        // Then
        assertEquals(0, expectedTotal.compareTo(actualTotal));
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistValue() {
        // Given - new customer with no wishlist items
        Customer newCustomer = new Customer("Alice", "Brown", "alice@example.com", "321 Elm St");
        newCustomer = customerService.addCustomer(newCustomer);
        
        // When, Then
        assertEquals(BigDecimal.ZERO, customerService.getWishlistTotalValue(newCustomer.getId().getId()));
    }

    @Test
    void shouldIdentifyVipCustomer() {
        // Given - customer with 10+ purchases becomes VIP
        UUID customerId = customer1.getId().getId();
        UUID productId = product1.getId().getId();
        
        // When - make 10 purchases to become VIP
        for (int i = 0; i < 10; i++) {
            boolean success = productService.purchaseProduct(customerId, productId, 1);
            assertTrue(success, "Purchase " + (i + 1) + " should succeed");
        }
        
        // Then
        assertTrue(customerService.isVipCustomer(customerId));
    }

    @Test
    void shouldIdentifyNonVipCustomer() {
        // Given - customer with less than 10 purchases
        UUID customerId = customer1.getId().getId();
        UUID productId = product1.getId().getId();
        
        // When - make only 5 purchases
        for (int i = 0; i < 5; i++) {
            boolean success = productService.purchaseProduct(customerId, productId, 1);
            assertTrue(success, "Purchase " + (i + 1) + " should succeed");
        }
        
        // Then
        assertFalse(customerService.isVipCustomer(customerId));
    }

    @Test
    void shouldThrowExceptionForNullCustomerId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getWishlistSize(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getWishlistTotalValue(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.isVipCustomer(null);
        });
    }

    @Test
    void shouldThrowExceptionForNullProductId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomersForProduct(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getBuyersOfProduct(null);
        });
    }

    @Test
    void shouldThrowExceptionForNullOrEmptySearchTerm() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.searchCustomers(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.searchCustomers("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.searchCustomers("   ");
        });
    }

    @Test
    void shouldThrowExceptionForNullOrEmptyLastName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomersByLastName(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomersByLastName("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomersByLastName("   ");
        });
    }

    @Test
    void shouldThrowExceptionForNullParameters() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.addCustomer(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerById(null);
        });
    }
}
