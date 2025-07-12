package training.a3.customer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.a3.customer.domain.Customer;
import training.a3.customer.domain.CustomerId;
import training.a3.customer.domain.CustomerRepository;
import training.a3.product.application.ProductService;
import training.a3.product.domain.Product;
import training.a3.product.domain.ProductId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private ProductService productService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ProductService productService) {
        this.customerRepository = customerRepository;
        this.productService = productService;
    }

    public Customer addCustomer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer is null");
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId is null");
        return customerRepository.findById(customerId);
    }

    public List<Customer> getCustomersByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        return customerRepository.findByLastName(lastName);
    }

    public List<Customer> getCustomersForProduct(ProductId productId) {
        if (productId == null) throw new IllegalArgumentException("ProductId is null");
        return customerRepository.findByWishlistContains(productId);
    }

    public int getWishlistSize(CustomerId customerId) {
        List<Product> products = productService.getProductsForCustomer(customerId);
        return products.size();
    }

    public BigDecimal getWishlistTotalValue(CustomerId customerId) {
        return productService.getTotalWishlistValue(customerId);
    }

    public List<Customer> searchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
        return customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            searchTerm, searchTerm);
    }

    public boolean isVipCustomer(CustomerId customerId) {
        Optional<Customer> customerOpt = getCustomerById(customerId);
        if (customerOpt.isEmpty()) return false;
        
        Customer customer = customerOpt.get();
        return customer.getPurchaseHistory().size() >= 10;
    }

    public List<Customer> getBuyersOfProduct(ProductId productId) {
        if (productId == null) throw new IllegalArgumentException("ProductId is null");
        return customerRepository.findByPurchaseHistoryContains(productId);
    }
}
