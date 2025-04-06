package tn.esprit.microservice.product_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add product
    public Product addProduct(Product product) {
        // Ensure the date is set when the product is added
        if (product.getDateAdded() == null) {
            product.setDateAdded(new java.util.Date());
        }
        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(int id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setSupplierEmail(updatedProduct.getSupplierEmail());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    // Delete product
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
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
    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    // Filter products by category
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    // Get product statistics
    public Map<Category, Long> getProductStats() {
        List<Product> allProducts = productRepository.findAll();

        long total = allProducts.size();

        // Group by Category
        Map<Category, Long> byCategory = allProducts.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        // You can also modify other statistics if needed
        Map<Category, Long> stats = new HashMap<>();
        stats.put(null, total); // For total count, you can keep the key as null or handle it as needed
        stats.putAll(byCategory);

        return stats;
    }

    // Get monthly product statistics (e.g., number of products added per month)
    public Map<String, Long> getMonthlyProductStats() {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM");
                            return sdf.format(p.getDateAdded());
                        },
                        Collectors.counting()
                ));
    }

    // Get price range of products based on their price
    private String getPriceRange(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (price < 50) {
            return "Low";
        } else if (price >= 50 && price < 150) {
            return "Medium";
        } else {
            return "High";
        }
    }

    // Get products by price range
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
