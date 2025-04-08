package tn.esprit.stock_service;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
public class Stock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int productId;  // Now storing product ID as a reference

    @Column(nullable = false)
    private int minQuantity = 1;  // Valeur par défaut de 1

    private int quantity;  // Current available quantity

    private String location;  // Storage location (e.g., "Main Warehouse - Shelf 3")

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastUpdated;  // Timestamp of the last stock update

    @Enumerated(EnumType.STRING)
    private StockStatus status;  // Status: Available, OutOfStock, Reserved

    private String notes;  // Optional remarks about the stock

    public Stock() {}

    public Stock(int productId,int minQuantity, int quantity, String location, StockStatus status, String notes) {
        this.productId = productId;
        this.minQuantity = minQuantity;
        this.quantity = quantity;
        this.location = location;
        this.status = status;
        this.notes = notes;
        this.lastUpdated = new java.util.Date();  // Setting the last updated time to now
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StockStatus.AVAILABLE;  // Default status
        }
        if (lastUpdated == null) {
            lastUpdated = new java.util.Date();  // Ensure last updated is set
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public StockStatus getStatus() {
        return status;
    }

    public void setStatus(StockStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}