package training.a3.product.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.a3.product.domain.Product;
import training.a3.product.domain.ProductId;
import training.a3.product.domain.ProductRepository;
import training.a3.customer.domain.Customer;
import training.a3.customer.domain.CustomerId;
import training.a3.customer.domain.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Product addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product is null");
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(ProductId productId) {
        if (productId == null) throw new IllegalArgumentException("ProductId is null");
        return productRepository.findById(productId);
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsForCustomer(UUID customerId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId is null");
        return productRepository.findByInterestedCustomerIdsContains(customerId);
    }

    public void addCustomerInterest(UUID customerId, UUID productId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId is null");
        if (productId == null) throw new IllegalArgumentException("ProductId is null");
        
        Optional<Product> productOpt = productRepository.findById(new ProductId(productId));
        Optional<Customer> customerOpt = customerRepository.findById(new CustomerId(customerId));
        
        if (productOpt.isPresent() && customerOpt.isPresent()) {
            Product product = productOpt.get();
            Customer customer = customerOpt.get();
            
            product.addInterestedCustomer(customerId);
            customer.addToWishlist(productId);
            
            productRepository.save(product);
            customerRepository.save(customer);
        }
    }

    public BigDecimal getTotalWishlistValue(UUID customerId) {
        List<Product> products = getProductsForCustomer(customerId);
        return products.stream()
                      .map(Product::getPrice)
                      .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Product> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        return productRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    public boolean purchaseProduct(UUID customerId, UUID productId, int quantity) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId is null");
        if (productId == null) throw new IllegalArgumentException("ProductId is null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        
        Optional<Product> productOpt = productRepository.findById(new ProductId(productId));
        Optional<Customer> customerOpt = customerRepository.findById(new CustomerId(customerId));
        
        if (productOpt.isEmpty() || customerOpt.isEmpty()) {
            return false;
        }
        
        Product product = productOpt.get();
        Customer customer = customerOpt.get();
        
        if (product.getStockQuantity() < quantity) {
            return false; // Not enough stock
        }
        
        product.reduceStock(quantity);
        customer.addToPurchaseHistory(productId);
        
        productRepository.save(product);
        customerRepository.save(customer);
        
        return true;
    }
}
