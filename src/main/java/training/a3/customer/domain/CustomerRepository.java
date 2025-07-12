package training.a3.customer.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
    List<Customer> findByFirstName(String firstName);
    
    List<Customer> findByLastName(String lastName);
    
    List<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE :productId MEMBER OF c.productWishlistIds")
    List<Customer> findByProductWishlistIdsContains(UUID productId);
    
    @Query("SELECT c FROM Customer c WHERE :productId MEMBER OF c.purchaseHistoryIds")
    List<Customer> findByPurchaseHistoryIdsContains(UUID productId);
    
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstNamePart, String lastNamePart);
}
