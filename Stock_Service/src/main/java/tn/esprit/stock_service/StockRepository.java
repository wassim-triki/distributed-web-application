package tn.esprit.stock_service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByStatus(StockStatus status);

    List<Stock> findByProductId(int productId);  // Optionally find by product

}