package tn.esprit.microservice.product_service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM");

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add product
    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getDateAdded() == null) {
            product.setDateAdded(new Date());
        }
        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(int id, Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Updated product cannot be null");
        }
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
                    existingProduct.setCategory(updatedProduct.getCategory());
                    existingProduct.setSupplierEmail(updatedProduct.getSupplierEmail());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + id));
    }

    // Delete product
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    // Get product by name
    public Optional<Product> getProductByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        Product product = productRepository.findByName(name);
        return Optional.ofNullable(product);
    }

    // Filter products by category
    public List<Product> getProductsByCategory(Category category) {
        if (category == null) {
            return Collections.emptyList();
        }
        return productRepository.findByCategory(category);
    }

    // Get product statistics
    public ProductStatsDTO getProductStats() {
        List<Product> allProducts = productRepository.findAll();
        long total = allProducts.size();
        Map<Category, Long> byCategory = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
        return new ProductStatsDTO(total, byCategory);
    }

    // Get monthly product statistics
    public Map<String, Long> getMonthlyProductStats() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            return new HashMap<>();
        }
        return allProducts.stream()
                .filter(p -> p.getDateAdded() != null) // Avoid NullPointerException
                .collect(Collectors.groupingBy(
                        p -> SDF.format(p.getDateAdded()),
                        Collectors.counting()
                ));
    }

    // Get products by price range
    // In ProductService
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice, Category category) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range: minPrice=" + minPrice + ", maxPrice=" + maxPrice);
        }
        if (category == null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
    }
}
class ProductStatsDTO {
    private final long total;
    private final Map<Category, Long> byCategory;

    public ProductStatsDTO(long total, Map<Category, Long> byCategory) {
        this.total = total;
        this.byCategory = byCategory != null ? byCategory : new HashMap<>();
    }

    public long getTotal() {
        return total;
    }

    public Map<Category, Long> getByCategory() {
        return byCategory;
    }
}