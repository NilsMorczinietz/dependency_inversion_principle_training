package training.a3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a3.customer.application.CustomerService;
import training.a3.customer.domain.Customer;
import training.a3.product.application.ProductService;
import training.a3.product.domain.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class CustomerProductTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    private Customer customer1, customer2, customer3;
    private Product product1, product2, product3;

    @BeforeEach
    public void setUp() {
        initializeCustomers();
        initializeProducts();
        setupCustomerInterests();
    }

    /**
     * Initializes the customers used in the tests.
     */
    private void initializeCustomers() {
        customer1 = new Customer("John", "Doe", "john.doe@example.com", "123 Main St");
        customer1 = customerService.addCustomer(customer1);
        
        customer2 = new Customer("Jane", "Smith", "jane.smith@example.com", "456 Oak Ave");
        customer2 = customerService.addCustomer(customer2);
        
        customer3 = new Customer("Bob", "Wilson", "bob.wilson@example.com", "789 Pine St");
        customer3 = customerService.addCustomer(customer3);
    }

    /**
     * Initializes the products used in the tests.
     */
    private void initializeProducts() {
        product1 = new Product("Laptop", "High-performance laptop", new BigDecimal("999.99"), 10, "Electronics");
        product1 = productService.addProduct(product1);
        
        product2 = new Product("Book", "Programming guide", new BigDecimal("49.99"), 50, "Books");
        product2 = productService.addProduct(product2);
        
        product3 = new Product("Headphones", "Noise-cancelling headphones", new BigDecimal("199.99"), 25, "Electronics");
        product3 = productService.addProduct(product3);
    }

    /**
     * Sets up customer interests in products.
     */
    private void setupCustomerInterests() {
        // Customer1 is interested in Laptop and Book
        productService.addCustomerInterest(customer1.getId().getId(), product1.getId().getId());
        productService.addCustomerInterest(customer1.getId().getId(), product2.getId().getId());
        
        // Customer2 is interested in Headphones
        productService.addCustomerInterest(customer2.getId().getId(), product3.getId().getId());
        
        // Customer3 has no interests (will be used for edge case tests)
    }

    @Test
    public void testCustomerWishlistSize() {
        // given, when, then
        assertEquals(2, customerService.getWishlistSize(customer1.getId().getId()));
        assertEquals(1, customerService.getWishlistSize(customer2.getId().getId()));
        assertEquals(0, customerService.getWishlistSize(customer3.getId().getId()));
    }

    @Test
    public void testCustomerWishlistTotalValue() {
        // Customer1: Laptop (999.99) + Book (49.99) = 1049.98
        BigDecimal expectedTotal1 = new BigDecimal("1049.98");
        assertEquals(0, expectedTotal1.compareTo(customerService.getWishlistTotalValue(customer1.getId().getId())));
        
        // Customer2: Headphones (199.99) = 199.99
        BigDecimal expectedTotal2 = new BigDecimal("199.99");
        assertEquals(0, expectedTotal2.compareTo(customerService.getWishlistTotalValue(customer2.getId().getId())));
        
        // Customer3: No products = 0.00
        assertEquals(BigDecimal.ZERO, customerService.getWishlistTotalValue(customer3.getId().getId()));
    }

    @Test
    public void testVipCustomerIdentification() {
        // Setup multiple purchases to test VIP status (VIP = >= 10 purchases)
        // Customer1 makes 10 purchases (should be VIP)
        for (int i = 0; i < 10; i++) {
            productService.purchaseProduct(customer1.getId().getId(), product2.getId().getId(), 1); // Buy books (sufficient stock)
        }
        
        // Customer2 makes only 1 purchase (should not be VIP)
        productService.purchaseProduct(customer2.getId().getId(), product2.getId().getId(), 1);
        
        // Test VIP status
        assertTrue(customerService.isVipCustomer(customer1.getId().getId()));
        assertFalse(customerService.isVipCustomer(customer2.getId().getId()));
        assertFalse(customerService.isVipCustomer(customer3.getId().getId()));
    }

    @Test
    public void testProductPurchaseWithStockUpdate() {
        // Test successful purchase with sufficient stock
        int initialStock = product1.getStockQuantity();
        boolean success = productService.purchaseProduct(customer1.getId().getId(), product1.getId().getId(), 2);
        
        assertTrue(success);
        
        // Verify stock was reduced
        Product updatedProduct = productService.getProductById(product1.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(initialStock - 2, updatedProduct.getStockQuantity());
    }

    @Test
    public void testProductPurchaseWithInsufficientStock() {
        // Try to buy more than available stock
        int quantity = product1.getStockQuantity() + 5;
        boolean success = productService.purchaseProduct(customer1.getId().getId(), product1.getId().getId(), quantity);
        
        assertFalse(success);
        
        // Verify stock remained unchanged
        Product unchangedProduct = productService.getProductById(product1.getId()).orElse(null);
        assertNotNull(unchangedProduct);
        assertEquals(10, unchangedProduct.getStockQuantity()); // Original stock
    }

    @Test
    public void testProductsForCustomer() {
        // Test products for customer1 (should have 2 products)
        var customer1Products = productService.getProductsForCustomer(customer1.getId().getId());
        assertEquals(2, customer1Products.size());
        assertTrue(customer1Products.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(customer1Products.stream().anyMatch(p -> p.getName().equals("Book")));
        
        // Test products for customer2 (should have 1 product)
        var customer2Products = productService.getProductsForCustomer(customer2.getId().getId());
        assertEquals(1, customer2Products.size());
        assertEquals("Headphones", customer2Products.get(0).getName());
        
        // Test products for customer3 (should have no products)
        var customer3Products = productService.getProductsForCustomer(customer3.getId().getId());
        assertTrue(customer3Products.isEmpty());
    }

    @Test
    public void testTotalWishlistValueCalculation() {
        // Test ProductService calculation
        BigDecimal customer1Total = productService.getTotalWishlistValue(customer1.getId().getId());
        BigDecimal expectedTotal = new BigDecimal("1049.98");
        assertEquals(0, expectedTotal.compareTo(customer1Total));
        
        BigDecimal customer2Total = productService.getTotalWishlistValue(customer2.getId().getId());
        BigDecimal expectedTotal2 = new BigDecimal("199.99");
        assertEquals(0, expectedTotal2.compareTo(customer2Total));
        
        BigDecimal customer3Total = productService.getTotalWishlistValue(customer3.getId().getId());
        assertEquals(BigDecimal.ZERO, customer3Total);
    }
}
