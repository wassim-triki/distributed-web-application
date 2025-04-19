package tn.esprit.microservice.product_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST controller for managing Product endpoints
 */
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
        logger.info("Fetching all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        logger.info("Fetching product by ID: {}", id);
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Product with ID={} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Add a new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            logger.info("Adding new product: {}", product.getName());
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
            logger.info("Updating product with ID: {}", id);
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
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
            logger.info("Deleting product with ID: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.warn("Product not found for deletion: ID={}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Get overall product statistics
    @GetMapping("/stats")
    public ResponseEntity<ProductService.ProductStatsDTO> getProductStats() {
        try {
            logger.info("Fetching overall product statistics");
            return ResponseEntity.ok(productService.getProductStats());
        } catch (Exception e) {
            logger.error("Failed to retrieve product stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get monthly product statistics
    @GetMapping("/stats/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyProductStats() {
        try {
            logger.info("Fetching monthly product statistics");
            return ResponseEntity.ok(productService.getMonthlyProductStats());
        } catch (Exception e) {
            logger.error("Error fetching monthly stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Category category) {
        logger.info("Fetching products by category: {}", category);
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // Get products by price range (with optional category)
    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam(required = false) Category category) {
        try {
            logger.info("Fetching products in price range: {} - {}, category: {}", minPrice, maxPrice, category);
            return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice, category));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid price range: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Search products by keyword in description
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        logger.info("Searching products with keyword: {}", keyword);
        return ResponseEntity.ok(productService.searchByKeyword(keyword));
    }

    // Generate QR code for a product
    @GetMapping("/{id}/generate-qr")
    public ResponseEntity<String> generateQRCode(@PathVariable int id) {
        logger.info("Generating QR code for product with ID: {}", id);

        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            String qrCodePath = productService.generateQRCodeForProduct(product.get());
            return ResponseEntity.ok("QR code generated and saved at: " + qrCodePath);
        } else {
            logger.warn("Product not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

}
