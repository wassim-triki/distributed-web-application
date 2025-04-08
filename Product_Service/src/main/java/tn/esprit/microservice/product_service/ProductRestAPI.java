package tn.esprit.microservice.product_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductRestAPI {

    private static final Logger logger = LoggerFactory.getLogger(ProductRestAPI.class);
    private final ProductService productService;

    @Autowired
    public ProductRestAPI(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid product data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (NoSuchElementException e) {
            logger.warn("Product not found for update: ID={}", id);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid update data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.warn("Product not found for deletion: ID={}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Get product statistics
    @GetMapping("/stats")
    public ResponseEntity<ProductStatsDTO> getProductStats() {
        try {
            ProductStatsDTO stats = productService.getProductStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Failed to retrieve product stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Category category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // In ProductRestAPI
    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam(required = false) Category category) {
        try {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, category);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid price range: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}