package tn.esprit.microservice.product_service;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name; // Product name

    @Column(length = 1000)
    private String description; // Product description

    private double price; // Product price

    private int stockQuantity; // Available stock for the product

    @Enumerated(EnumType.STRING)
    private Category category; // Category for the product (e.g., Electronics, Clothing)

    private String supplierEmail; // Email of the product supplier

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded; // Date when the product was added

    public Product() {}

    public Product(String name, String description, double price, int stockQuantity, Category category, String supplierEmail) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.supplierEmail = supplierEmail;
        this.dateAdded = new Date(); // Set the date when the product is created
    }

    @PrePersist
    public void prePersist() {
        if (dateAdded == null) {
            dateAdded = new Date(); // Ensure date is set if not provided
        }
    }

    // Getters and Setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
