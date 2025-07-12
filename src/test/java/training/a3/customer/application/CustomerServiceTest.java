package training.a3.customer.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import training.a3.customer.domain.CustomerRepository;
import training.a3.product.application.ProductService;
import training.a3.product.domain.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductService productService;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, productService);
    }

    @Test
    void shouldReturnCorrectWishlistSize() {
        // Given
        UUID customerId = UUID.randomUUID();
        Product product1 = new Product("Product 1", "Great electronics item", new BigDecimal("99.99"), 10, "Electronics");
        Product product2 = new Product("Product 2", "Interesting book", new BigDecimal("29.99"), 5, "Books");
        Product product3 = new Product("Product 3", "Stylish clothing", new BigDecimal("49.99"), 20, "Clothing");
        
        List<Product> wishlistProducts = Arrays.asList(product1, product2, product3);
        
        when(productService.getProductsForCustomer(customerId)).thenReturn(wishlistProducts);

        // When
        int wishlistSize = customerService.getWishlistSize(customerId);

        // Then
        assertEquals(3, wishlistSize);
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistItems() {
        // Given
        UUID customerId = UUID.randomUUID();
        
        when(productService.getProductsForCustomer(customerId)).thenReturn(Collections.emptyList());

        // When
        int wishlistSize = customerService.getWishlistSize(customerId);

        // Then
        assertEquals(0, wishlistSize);
    }

    @Test
    void shouldReturnCorrectWishlistTotalValue() {
        // Given
        UUID customerId = UUID.randomUUID();
        BigDecimal expectedTotalValue = new BigDecimal("179.97"); // 99.99 + 29.99 + 49.99
        
        when(productService.getTotalWishlistValue(customerId)).thenReturn(expectedTotalValue);

        // When
        BigDecimal totalValue = customerService.getWishlistTotalValue(customerId);

        // Then
        assertEquals(expectedTotalValue, totalValue);
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistValue() {
        // Given
        UUID customerId = UUID.randomUUID();
        
        when(productService.getTotalWishlistValue(customerId)).thenReturn(BigDecimal.ZERO);

        // When
        BigDecimal totalValue = customerService.getWishlistTotalValue(customerId);

        // Then
        assertEquals(BigDecimal.ZERO, totalValue);
    }

    @Test
    void shouldThrowExceptionForNullCustomerId() {
        // When & Then - Direct null validation in service methods
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

        // When & Then - addCustomer with null
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.addCustomer(null);
        });

        // When & Then - getCustomerById with null
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerById(null);
        });
    }
}
