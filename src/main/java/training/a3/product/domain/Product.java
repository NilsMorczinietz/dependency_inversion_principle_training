package training.a3.product.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Product {
    
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private ProductId id;
    
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> interestedCustomerIds;

    public Product(String name, String description, BigDecimal price, int stockQuantity, String category) {
        this.id = new ProductId();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.interestedCustomerIds = new ArrayList<>();
    }

    // Business methods
    public void addInterestedCustomer(UUID customerId) {
        if (customerId != null && !interestedCustomerIds.contains(customerId)) {
            interestedCustomerIds.add(customerId);
        }
    }

    public void removeInterestedCustomer(UUID customerId) {
        interestedCustomerIds.remove(customerId);
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        this.stockQuantity -= quantity;
    }

    // Override Lombok getters for collections to return defensive copies
    public List<UUID> getInterestedCustomerIds() {
        return List.copyOf(interestedCustomerIds);
    }
}
