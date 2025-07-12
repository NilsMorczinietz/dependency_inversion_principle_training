package training.a3.customer.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Customer {
    
    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private CustomerId id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> productWishlistIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UUID> purchaseHistoryIds;

    public Customer(String firstName, String lastName, String email, String address) {
        this.id = new CustomerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.productWishlistIds = new ArrayList<>();
        this.purchaseHistoryIds = new ArrayList<>();
    }

    public CustomerId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addToWishlist(UUID productId) {
        if (productId != null && !productWishlistIds.contains(productId)) {
            productWishlistIds.add(productId);
        }
    }

    public void removeFromWishlist(UUID productId) {
        productWishlistIds.remove(productId);
    }

    public void addToPurchaseHistory(UUID productId) {
        if (productId != null) {
            purchaseHistoryIds.add(productId);
        }
    }

    public boolean hasProductInWishlist(UUID productId) {
        return productWishlistIds.contains(productId);
    }

    public boolean hasPurchased(UUID productId) {
        return purchaseHistoryIds.contains(productId);
    }

    public int getWishlistSize() {
        return productWishlistIds.size();
    }

    public int getPurchaseCount() {
        return purchaseHistoryIds.size();
    }
    
    // Override Lombok getters for collections to return defensive copies
    public List<UUID> getProductWishlistIds() {
        return List.copyOf(productWishlistIds);
    }
    
    public List<UUID> getPurchaseHistoryIds() {
        return List.copyOf(purchaseHistoryIds);
    }
}
