package tn.esprit.microservice.product_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // You can define custom query methods here if needed.
    // For example, to find a product by its name:
    Product findByName(String name);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    // Or to find products by category:
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryAndPriceBetween(Category category, double minPrice, double maxPrice);
}
