package tn.esprit.ordreservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity

@Table(name = "orders") // ✅ Évite le problème de mot réservé
@EntityListeners(AuditingEntityListener.class)  // Ajouté pour l'auditing

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assure l'auto-incrémentation
    private Integer id;

    @Column(unique = true,  nullable = false)
    private String reference;

    private String customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderLine> orderLines;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @Transient // Ne sera pas stocké en base, c’est juste calculé dynamiquement
    @Column(nullable = false)
    private BigDecimal totalAmount;

    public BigDecimal getTotalAmount() {
        if (orderLines == null) return BigDecimal.ZERO;

        return orderLines.stream()
                .map(line -> line.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Constructeur vide
    public Order() {
    }

    // Constructeur paramétré sans 'id' (car 'id' est généré automatiquement)
    public Order(String reference, String customerId, List<OrderLine> orderLines) {
        this.reference = reference;
        this.customerId = customerId;
        this.orderLines = orderLines;

    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
