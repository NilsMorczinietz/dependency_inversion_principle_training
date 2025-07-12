package training.a3.customer.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a3.product.domain.ProductId;

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
    
    public void setId(UUID id){
        this.id = new CustomerId(id);
    }
    
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<ProductId> wishlist;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<ProductId> purchaseHistory;

    public Customer(String firstName, String lastName, String email, String address) {
        this.id = new CustomerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.wishlist = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    public CustomerId getId() {
        return id;
    }

    public void setId(CustomerId id) {
        this.id = id;
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

    public void addToWishlist(training.a3.product.domain.Product product) {
        if (product != null && !wishlist.contains(product.getId())) {
            wishlist.add(product.getId());
        }
    }

    public void removeFromWishlist(ProductId productId) {
        wishlist.remove(productId);
    }

    public void addToPurchaseHistory(training.a3.product.domain.Product product) {
        if (product != null) {
            purchaseHistory.add(product.getId());
        }
    }

    public boolean hasProductInWishlist(ProductId productId) {
        return wishlist.contains(productId);
    }

    public boolean hasPurchased(ProductId productId) {
        return purchaseHistory.contains(productId);
    }

    public int getWishlistSize() {
        return wishlist.size();
    }

    public int getPurchaseCount() {
        return purchaseHistory.size();
    }
    
    // Override Lombok getters for collections to return defensive copies
    public List<ProductId> getWishlist() {
        return List.copyOf(wishlist);
    }
    
    public List<ProductId> getPurchaseHistory() {
        return List.copyOf(purchaseHistory);
    }
}
