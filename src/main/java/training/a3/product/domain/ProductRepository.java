package training.a3.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
    List<Product> findByName(String name);
    
    List<Product> findByCategory(String category);
    
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE :customerId MEMBER OF p.interestedCustomerIds")
    List<Product> findByInterestedCustomerIdsContains(UUID customerId);
    
    List<Product> findByNameContainingIgnoreCase(String namePart);
    
    List<Product> findByStockQuantityGreaterThan(int quantity);
}
