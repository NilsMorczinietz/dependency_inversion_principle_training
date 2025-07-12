package training.a3;

/**
 * Business Logic Tests for A3 E-Commerce Domain
 * 
 * These tests verify the core business logic functionality independent of 
 * cyclic dependencies. They should remain green before and after DIP refactoring.
 * 
 * Test Coverage:
 * - Product entity logic (stock management, properties)
 * - Customer entity logic (wishlist, purchase history) 
 * - ID value objects (ProductId, CustomerId)
 * - Converters (ProductIdConverter, CustomerIdConverter)
 * 
 * Run these tests to verify that your DIP refactoring didn't break existing functionality:
 * 
 * mvn test -Dtest="training.a3.*.domain.*Test"
 */
public class BusinessLogicTestSuite {
    // This class serves as documentation for the business logic tests.
    // The actual tests are in the domain packages:
    // - training.a3.product.domain.*Test
    // - training.a3.customer.domain.*Test
}
