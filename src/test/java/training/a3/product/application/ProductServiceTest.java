package training.a3.product.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import training.a3.customer.domain.Customer;
import training.a3.customer.domain.CustomerId;
import training.a3.customer.domain.CustomerRepository;
import training.a3.product.domain.Product;
import training.a3.product.domain.ProductId;
import training.a3.product.domain.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, customerRepository);
    }

    @Test
    void shouldReturnCorrectTotalWishlistValue() {
        // Given
        UUID customerId = UUID.randomUUID();
        Product product1 = new Product("Product 1", "Great electronics item", new BigDecimal("99.99"), 10, "Electronics");
        Product product2 = new Product("Product 2", "Interesting book", new BigDecimal("29.99"), 5, "Books");
        Product product3 = new Product("Product 3", "Stylish clothing", new BigDecimal("49.99"), 20, "Clothing");
        
        List<Product> wishlistProducts = Arrays.asList(product1, product2, product3);
        
        when(productRepository.findByInterestedCustomerIdsContains(customerId)).thenReturn(wishlistProducts);

        // When
        BigDecimal totalValue = productService.getTotalWishlistValue(customerId);

        // Then
        assertEquals(new BigDecimal("179.97"), totalValue);
    }

    @Test
    void shouldReturnZeroWhenCustomerHasNoWishlistProducts() {
        // Given
        UUID customerId = UUID.randomUUID();
        
        when(productRepository.findByInterestedCustomerIdsContains(customerId)).thenReturn(Collections.emptyList());

        // When
        BigDecimal totalValue = productService.getTotalWishlistValue(customerId);

        // Then
        assertEquals(BigDecimal.ZERO, totalValue);
    }

    @Test
    void shouldReturnCorrectProductsForCustomer() {
        // Given
        UUID customerId = UUID.randomUUID();
        Product product1 = new Product("Product 1", "Great electronics item", new BigDecimal("99.99"), 10, "Electronics");
        Product product2 = new Product("Product 2", "Interesting book", new BigDecimal("29.99"), 5, "Books");
        
        List<Product> customerProducts = Arrays.asList(product1, product2);
        
        when(productRepository.findByInterestedCustomerIdsContains(customerId)).thenReturn(customerProducts);

        // When
        List<Product> products = productService.getProductsForCustomer(customerId);

        // Then
        assertEquals(2, products.size());
        assertEquals(customerProducts, products);
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoProducts() {
        // Given
        UUID customerId = UUID.randomUUID();
        
        when(productRepository.findByInterestedCustomerIdsContains(customerId)).thenReturn(Collections.emptyList());

        // When
        List<Product> products = productService.getProductsForCustomer(customerId);

        // Then
        assertTrue(products.isEmpty());
    }

    @Test
    void shouldSuccessfullyPurchaseProductWithSufficientStock() {
        // Given
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        Product product = new Product("Test Product", "Test description", new BigDecimal("50.00"), 10, "Test");
        
        when(customerRepository.findById(any(CustomerId.class))).thenReturn(Optional.of(customer));
        when(productRepository.findById(any(ProductId.class))).thenReturn(Optional.of(product));

        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);

        // Then
        assertTrue(success);
        assertEquals(8, product.getStockQuantity()); // 10 - 2 = 8
    }

    @Test
    void shouldFailToPurchaseProductWithInsufficientStock() {
        // Given
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 15; // More than available stock
        
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        Product product = new Product("Test Product", "Test description", new BigDecimal("50.00"), 10, "Test");
        
        when(customerRepository.findById(any(CustomerId.class))).thenReturn(Optional.of(customer));
        when(productRepository.findById(any(ProductId.class))).thenReturn(Optional.of(product));

        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);

        // Then
        assertFalse(success);
        assertEquals(10, product.getStockQuantity()); // Stock should remain unchanged
    }

    @Test
    void shouldFailToPurchaseProductWhenCustomerNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        
        Product product = new Product("Test Product", "Test description", new BigDecimal("50.00"), 10, "Test");
        
        when(customerRepository.findById(any(CustomerId.class))).thenReturn(Optional.empty());
        when(productRepository.findById(any(ProductId.class))).thenReturn(Optional.of(product));

        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);

        // Then
        assertFalse(success);
    }

    @Test
    void shouldFailToPurchaseProductWhenProductNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        
        when(customerRepository.findById(any(CustomerId.class))).thenReturn(Optional.of(customer));
        when(productRepository.findById(any(ProductId.class))).thenReturn(Optional.empty());

        // When
        boolean success = productService.purchaseProduct(customerId, productId, quantity);

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
