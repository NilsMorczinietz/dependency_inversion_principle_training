package training.a3.customer.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import training.a3.product.domain.ProductId;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
    List<Customer> findByFirstName(String firstName);
    
    List<Customer> findByLastName(String lastName);
    
    List<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE :productId MEMBER OF c.wishlist")
    List<Customer> findByWishlistContains(ProductId productId);
    
    @Query("SELECT c FROM Customer c WHERE :productId MEMBER OF c.purchaseHistory")
    List<Customer> findByPurchaseHistoryContains(ProductId productId);
    
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstNamePart, String lastNamePart);
}
