package tn.esprit.microservice.product_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByPriceBetween(double min, double max);
    List<Product> findByCategoryAndPriceBetween(Category category, double min, double max);
    List<Product> findByCategory(Category category);
    Product findByName(String name);
    List<Product> findByDescriptionContainingIgnoreCase(String keyword); // for optional search

}
